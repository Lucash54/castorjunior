package castorjunior;

import static org.apache.spark.sql.functions.col;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.spark_project.guava.collect.Lists;

public class SparkMLRandomForest {
	protected static int y = 0;
	
	public static void main(String[] args) {

		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, et le nom de la variable explicative

		Scanner saisieUtilisateur = new Scanner(System.in);

		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv,
		// le nom de la variable explicative
		// le nombre d'arbres utilisé

		System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/pages.csv ou ./src/iris.csv, ou n'importe quelle adresse contenant un csv) :");
		String strAdresse = saisieUtilisateur.next();

		System.out.println("Veuillez saisir le nom de la variable à expliquer (il faut qu'elle existe dans le csv et qu'elle respecte la casse, sinon le code ne marchera pas):");
		String strVariable = saisieUtilisateur.next();

		System.out.println("Veuillez saisir la proportion d'apprentissage (ex: pour 70% d'app et 30% de test, taper 0.7) :");
		String pA = saisieUtilisateur.next();
		double propApp = Double.parseDouble(pA);
		
		System.out.println("Veuillez saisir le nombre d'arbres voulu :");
		String nbarbre = saisieUtilisateur.next();
		int nbTrees = Integer.parseInt(nbarbre);

		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		JavaSparkContext spContext = SparkConnection.getContext();
		SparkSession spSession = SparkConnection.getSession();
		
		/*--------------------------------------------------------------------------
		Load Data
		--------------------------------------------------------------------------*/
		Dataset<Row> irisDf = spSession.read()
				.option("header","true")
				.csv(strAdresse);
		irisDf.show(5);
		irisDf.printSchema();
		
		/*--------------------------------------------------------------------------
		Cleanse Data
		--------------------------------------------------------------------------*/	
		//Convert all data types as double; Change missing values to standard ones.
		
		//Create the schema for the data to be loaded into Dataset.
		String[] header = irisDf.columns();
		String varY = strVariable;
		int l= header.length;
		String[] varsX = new String[l-2];
		int j=0;
		for(int i = 1; i < l; i++){
			String s = header[i];
			if(s.equals(varY)) {
				y=i;
			}else {
				varsX[j]=s;
				j++;
			}
		}
		StructField[] fields = new StructField[varsX.length+1];
		int i=0;
		for (String s : varsX){
			fields[i]=DataTypes.createStructField(s, DataTypes.DoubleType, false);
			i++;
		}
		fields[i]=DataTypes.createStructField(varY, DataTypes.StringType, false);
		StructType irisSchema = DataTypes
				.createStructType(Lists.newArrayList(fields));

		//Change data frame back to RDD
		JavaRDD<Row> rdd1 = irisDf.toJavaRDD().repartition(2);
		
		//Function to map.

		JavaRDD<Row> rdd2 = rdd1.map( new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {
				List<Object> mlist = new ArrayList<Object>();
				for(int i = 1; i<=varsX.length+1;i++) {
					if(i!=y) {
						mlist.add(Double.valueOf(iRow.getString(i)));
					}
				}
				mlist.add(iRow.getString(y));
				Row retRow = RowFactory.create(mlist.toArray());
				
				return retRow;
			}
		});

		//Create Data Frame back.
		Dataset<Row> irisCleansedDf = spSession.createDataFrame(rdd2, irisSchema);
		System.out.println("Transformed Data :");
		System.out.println(irisCleansedDf.toString());
		irisCleansedDf.show(5);
		
		/*--------------------------------------------------------------------------
		Analyze Data
		--------------------------------------------------------------------------*/
		
		//Add an index using string indexer.
		
		StringIndexer indexer = new StringIndexer()
				  .setInputCol(varY)
				  .setOutputCol("IND_SPECIES");
		
		StringIndexerModel siModel = indexer.fit(irisCleansedDf);
		Dataset<Row> indexedIris = siModel.transform(irisCleansedDf);
								
		indexedIris.groupBy(col(varY),col("IND_SPECIES")).count().show();
		
		//Perform correlation analysis
		for ( StructField field : irisSchema.fields() ) {
			if ( ! field.dataType().equals(DataTypes.StringType)) {
				System.out.println( "Correlation between IND_SPECIES and " + field.name()
				 	+ " = " + indexedIris.stat().corr("IND_SPECIES", field.name()) );
			}
		}
		
		/*--------------------------------------------------------------------------
		Prepare for Machine Learning. 
		--------------------------------------------------------------------------*/
		
		//Convert data to labeled Point structure
		JavaRDD<Row> rdd3 = indexedIris.toJavaRDD().repartition(2);
		
		JavaRDD<LabeledPoint> rdd4 = rdd3.map( new Function<Row, LabeledPoint>() {

			@Override
			public LabeledPoint call(Row iRow) throws Exception {
				double[] mlist = new double [varsX.length];
				int j = 0;
				for(int i = 0; i<varsX.length;i++) {
					mlist[i]=iRow.getDouble(i);
				}
				Vector vect = Vectors.dense(mlist);
				LabeledPoint lp = new LabeledPoint(iRow.getDouble(varsX.length+1), vect);
				return lp;
				
			}

		});

		Dataset<Row> irisLp = spSession.createDataFrame(rdd4, LabeledPoint.class);
		irisLp.show(5);
		
		// Split the data into training and test sets (30% held out for testing).
		Dataset<Row>[] splits = irisLp.randomSplit(new double[]{propApp, 1-propApp});
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];
		
		/*--------------------------------------------------------------------------
		Perform machine learning. 
		--------------------------------------------------------------------------*/	

		//Create the object
		// Train a DecisionTree model.
		RandomForestClassifier rf = new RandomForestClassifier()
				  .setLabelCol("label")
				  .setFeaturesCol("features")
				  .setNumTrees(nbTrees);
		
		// Convert indexed labels back to original labels.
		IndexToString labelConverter = new IndexToString()
				  .setInputCol("label")
				  .setOutputCol("labelStr")
				  .setLabels(siModel.labels());
		
		IndexToString predConverter = new IndexToString()
				  .setInputCol("prediction")
				  .setOutputCol("predictionStr")
				  .setLabels(siModel.labels());

		RandomForestClassificationModel rfm = rf.fit(trainingData);
		
		//Predict on test data
		Dataset<Row> rawPredictions = rfm.transform(testData);
		Dataset<Row> predictions = predConverter.transform(
									labelConverter.transform(rawPredictions));
		
		//View results
		System.out.println("Result sample :");
		predictions.select("labelStr", "predictionStr", "features").show(5);

		//View confusion matrix
		System.out.println("Confusion Matrix :");
		predictions.groupBy(col("labelStr"), col("predictionStr")).count().show();
		
		//Accuracy computation
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
				  .setLabelCol("label")
				  .setPredictionCol("prediction")
				  .setMetricName("accuracy");
				double accuracy = evaluator.evaluate(predictions);
				System.out.println("Accuracy = " + Math.round( accuracy * 100) + " %" );
				
	}
}
