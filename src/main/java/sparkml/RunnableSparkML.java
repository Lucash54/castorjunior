package sparkml;

import static org.apache.spark.sql.functions.col;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.spark_project.guava.collect.Lists;

import scala.Serializable;

@SuppressWarnings("serial")
public class RunnableSparkML implements Serializable{

	public RunnableSparkML() {}

	protected static int y = 0;

	public double runSpark(SparkML sp) {
		// On récupère les données
		String strAdresse = sp.getStrAdresse();
		String strVariable = sp.getStrVariable();
		double propApp = sp.getPropApp();
		
		// On commence le travail en SparkML : On se connecte grâce à une SparkConnection
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		SparkSession spSession = SparkConnection.getSession();

		// On charge les données
		Dataset<Row> data = spSession.read()
				.option("header","true")
				.csv(strAdresse);
		// On affiche les 5 premières lignes
		data.show(5);

		// On nettoie les données : on convertit les types en double	
		String[] header = data.columns();
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

		// On crée le schéma des données
		StructType dataSchema = DataTypes
				.createStructType(Lists.newArrayList(fields));

		// On crée les rdd pour les transformer en dataset
		JavaRDD<Row> rdd1 = data.toJavaRDD().repartition(2);
		JavaRDD<Row> rdd2 =  rdd1.map( new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {
				List<Object> mlist = new ArrayList<Object>();
				for(int i = 1; i<=varsX.length+1;i++) {
					if(i!=y) {
						String aux = iRow.getString(i);
						if(aux.equals("true") || aux.equals("false")){
							boolean aux2 = Boolean.parseBoolean(aux);
							double bin = aux2 ? 1 : 0;
							mlist.add(Double.valueOf(bin));
						}else {
							mlist.add(Double.valueOf(aux));
						}
					}
				}
				mlist.add(iRow.getString(y));
				Row retRow = RowFactory.create(mlist.toArray());

				return retRow;
			}
		});

		// Création du dataset nettoyé
		Dataset<Row> dataCleansedDf = spSession.createDataFrame(rdd2, dataSchema);

		// On affiche les données sur lesquelles nous allons pouvoir travailler
		System.out.println("Transformed Data :");
		dataCleansedDf.show(5);

		// ANALYSE

		String codeVarY = "code_"+varY;
		StringIndexer indexer = new StringIndexer()
				.setInputCol(varY)
				.setOutputCol(codeVarY);

		StringIndexerModel siModel = indexer.fit(dataCleansedDf);
		Dataset<Row> indexedData = siModel.transform(dataCleansedDf);

		indexedData.groupBy(col(varY),col(codeVarY)).count().show();


		// Préparation pour le machine learning

		// On rassemble les données en 1 colonne avec la liste des valeurs des var et 1 colonne avec le label de la var à expliquer 
		JavaRDD<Row> rdd3 = indexedData.toJavaRDD().repartition(2);
		JavaRDD<LabeledPoint> rdd4 = rdd3.map( new Function<Row, LabeledPoint>() {

			@Override
			public LabeledPoint call(Row iRow) throws Exception {
				double[] mlist = new double [varsX.length];
				for(int i = 0; i<varsX.length;i++) {
					mlist[i]=iRow.getDouble(i);
				}
				Vector vect = Vectors.dense(mlist);
				LabeledPoint lp = new LabeledPoint(iRow.getDouble(varsX.length+1), vect);
				return lp;

			}

		});

		Dataset<Row> dataLp = spSession.createDataFrame(rdd4, LabeledPoint.class);
		dataLp.show(5);

		// On split les données en données apprentissage / test
		Dataset<Row>[] splits = dataLp.randomSplit(new double[]{propApp, 1-propApp});
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];

		// On crée une SparkMLMethod et selon l'algo que l'utilisateur a choisit, on va dans la classe fille de SparkMLMethod correspondante
		SparkMLMethod sm = null;
		String algo = sp.getAlgo();
		int nbTrees = sp.getNbTrees();
		if(algo.equals("c")) {
			// Arbre de Classification
	    	System.out.println("Cart choisi!");
			sm = new SparkMLDecisionTree(siModel, trainingData, testData);
		}

		if(algo.equals("r")) {
			// RandomForest
	    	System.out.println("RandomForest choisi!");
			sm = new SparkMLRandomForest(siModel, trainingData, testData, nbTrees);
		}

		if(algo.equals("s")) {
			// SVM
	    	System.out.println("SVM choisi!");
			sm = new SparkMLSVM(siModel, trainingData, testData);
		}

		double accuracy = sm.run();
		return accuracy;
	}
}
