package castorjunior;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.functions.*;

import castorjunior.SparkConnection;
import org.apache.spark.api.java.JavaRDD;

public class SparkMLDecisionTreesDemo { 


	public static void main(String[] args) {
		
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		JavaSparkContext spContext = SparkConnection.getContext();
		SparkSession spSession = SparkConnection.getSession();
		
		/*--------------------------------------------------------------------------
		Load Data
		--------------------------------------------------------------------------*/
		Dataset<Row> irisDf = spSession.read()
				.option("header","true")
				.csv("src/iris.csv");
		irisDf.show(5);
		irisDf.printSchema();
		
		/*--------------------------------------------------------------------------
		Cleanse Data
		--------------------------------------------------------------------------*/	
		//Convert all data types as double; Change missing values to standard ones.
		
		//Create the schema for the data to be loaded into Dataset.
		StructType irisSchema = DataTypes
				.createStructType(new StructField[] {
						DataTypes.createStructField("SEPAL_LENGTH", DataTypes.DoubleType, false),
						DataTypes.createStructField("SEPAL_WIDTH", DataTypes.DoubleType, false),
						DataTypes.createStructField("PETAL_LENGTH", DataTypes.DoubleType, false),
						DataTypes.createStructField("PETAL_WIDTH", DataTypes.DoubleType, false),
						DataTypes.createStructField("SPECIES", DataTypes.StringType, false) 
					});

		//Change data frame back to RDD
		JavaRDD<Row> rdd1 = irisDf.toJavaRDD().repartition(2);
		
		//Function to map.
		JavaRDD<Row> rdd2 = rdd1.map( new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {
				
				Row retRow = RowFactory.create( Double.valueOf(iRow.getString(1)), 
								Double.valueOf(iRow.getString(2)), 
								Double.valueOf(iRow.getString(3)), 
								Double.valueOf(iRow.getString(4)), 								
								iRow.getString(5)
						);
				
				return retRow;
			}

		});
		
		//Create Data Frame back.
		Dataset<Row> irisCleansedDf = spSession.createDataFrame(rdd2, irisSchema);
		System.out.println("Transformed Data :");
		irisCleansedDf.show(5);
		
		/*--------------------------------------------------------------------------
		Analyze Data
		--------------------------------------------------------------------------*/
		
		//Add an index using string indexer.
		
		StringIndexer indexer = new StringIndexer()
				  .setInputCol("SPECIES")
				  .setOutputCol("IND_SPECIES");
		
		StringIndexerModel siModel = indexer.fit(irisCleansedDf);
		Dataset<Row> indexedIris = siModel.transform(irisCleansedDf);
								
		indexedIris.groupBy(col("SPECIES"),col("IND_SPECIES")).count().show();
		
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
				
				LabeledPoint lp = new LabeledPoint(iRow.getDouble(5) , 
									Vectors.dense(iRow.getDouble(0),
											iRow.getDouble(1),
											iRow.getDouble(2),
											iRow.getDouble(3)));
				
				return lp;
			}

		});

		Dataset<Row> irisLp = spSession.createDataFrame(rdd4, LabeledPoint.class);
		irisLp.show(5);
		
		// Split the data into training and test sets (30% held out for testing).
		Dataset<Row>[] splits = irisLp.randomSplit(new double[]{0.7, 0.3});
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];
		
		/*--------------------------------------------------------------------------
		Perform machine learning. 
		--------------------------------------------------------------------------*/	
		
		//Create the object
		// Train a DecisionTree model.
		DecisionTreeClassifier dt = new DecisionTreeClassifier()
		  .setLabelCol("label")
		  .setFeaturesCol("features");

		// Convert indexed labels back to original labels.
		IndexToString labelConverter = new IndexToString()
				  .setInputCol("label")
				  .setOutputCol("labelStr")
				  .setLabels(siModel.labels());
		
		IndexToString predConverter = new IndexToString()
				  .setInputCol("prediction")
				  .setOutputCol("predictionStr")
				  .setLabels(siModel.labels());
		
		DecisionTreeClassificationModel dtModel = dt.fit(trainingData);
		
		//Predict on test data
		Dataset<Row> rawPredictions = dtModel.transform(testData);
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