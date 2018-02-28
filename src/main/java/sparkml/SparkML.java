package sparkml;

import static org.apache.spark.sql.functions.col;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

import castorjunior.SparkConnection;
import rengine.RengineCART;
import rengine.RengineMethod;
import rengine.RengineRandomForest;
import rengine.RengineSVM;

/**
 * Cette classe SparkML permet de calculer, grâce à 3 méthodes (arbre de classification, random forest et svm),
 * l'accuracy du modèle sur les données.
 *
 * Consignes : Le fichier est un csv. Les variables explicatives doivent être numériques. La première colonne est un identifiant ne
 * servant pas aux calculs. Pour le svm, la variable à expliquer doit être binaire. Les noms de variables ne doivent pas contenir de point.
 */



public class SparkML {
	protected static int y = 0;

	public static void main(String[] args) {

		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, le nom de la variable explicative,
		//ainsi que le pourcentage d'apprentissage 

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		//System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/pages.csv ou ./src/iris.csv, ou n'importe quelle adresse contenant un csv) :");
		//String strAdresse = saisieUtilisateur.next();

		//System.out.println("Veuillez saisir le nom de la variable à expliquer (il faut qu'elle existe dans le csv et qu'elle respecte la casse, sinon le code ne marchera pas):");
		//String strVariable = saisieUtilisateur.next();

		//System.out.println("Veuillez saisir la proportion d'apprentissage (ex: pour 70% d'app et 30% de test, taper 0.7) :");
		//String pA = saisieUtilisateur.next();
		//double propApp = Double.parseDouble(pA);
		String strAdresse = "src/pages.csv";
		String strVariable = "nbPages";
		double propApp = 0.7;
		String varBool = "ACK,BIB,BOLD_ACK,BREF,EMAIL,JS_FOOTNOTESIZE,JS_SCRIPTSIZE,JS_STYLE,JS_TINY,LONG_ACK,LONG_AFFILIATION,PARAGRAPH_ACK,PL_FOOTNOTE,VARY_LATEX";
		
		// On commence le travail en SparkML : ON se connecte grâce à une SarkConnection
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
		SparkSession spSession = SparkConnection.getSession();
		
		// On charge les données
		Dataset<Row> data = spSession.read()
				.option("header","true")
				.csv(strAdresse);
		// On affiche les 5 premières lignes
		data.show(5);
		//data.printSchema();
		
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
		String test2 = "";
		System.out.println(test2.contains("Ceci"));
		/*ArrayList<String> test=new ArrayList<String>(2);
		test.contains("ef");*/
		StructField[] fields = new StructField[varsX.length+1];
		int i=0;
		for (String s : varsX){
			if(varBool.contains(s)) {
				fields[i]=DataTypes.createStructField(s, DataTypes.BooleanType, false);
			}else {
				fields[i]=DataTypes.createStructField(s, DataTypes.DoubleType, false);
			}
			i++;
		}
		fields[i]=DataTypes.createStructField(varY, DataTypes.StringType, false);
		
		// On crée le schéma des données
		StructType dataSchema = DataTypes
				.createStructType(Lists.newArrayList(fields));

		// On crée les rdd pour les transformer en dataset
		JavaRDD<Row> rdd1 = data.toJavaRDD().repartition(2);
		System.out.println(varsX[0] + " ; " +varsX[1] + " ; " +varsX[2] + " ; " +varsX[varsX.length-1]);
		JavaRDD<Row> rdd2 = rdd1.map( new Function<Row, Row>() {

			@Override
			public Row call(Row iRow) throws Exception {
				List<Object> mlist = new ArrayList<Object>();
				int j = 0;
				for(int i = 1; i<=varsX.length+1;i++) {
					if(i!=y) {
						if(varBool.contains(varsX[j])){
							mlist.add(Boolean.valueOf(iRow.getString(i)));
						}else {
							mlist.add(Double.valueOf(iRow.getString(i)));
						}
						j++;
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
		System.out.println(dataCleansedDf.toString());
		dataCleansedDf.show(5);
		
		// ANALYSE
		
		String codeVarY = "code_"+varY;
		StringIndexer indexer = new StringIndexer()
				  .setInputCol(varY)
				  .setOutputCol(codeVarY);
		
		StringIndexerModel siModel = indexer.fit(dataCleansedDf);
		Dataset<Row> indexedData = siModel.transform(dataCleansedDf);
								
		indexedData.groupBy(col(varY),col(codeVarY)).count().show();
		
		// Corrélations :
		/*for ( StructField field : dataSchema.fields() ) {
			if ( ! field.dataType().equals(DataTypes.StringType)) {
				System.out.println( "Correlation between "+ codeVarY +" and " + field.name()
				 	+ " = " + indexedData.stat().corr(codeVarY, field.name()) );
			}
		}*/
		
		// Préparation pour le machine learning
		
		// On rassemble les données en 1 colonne avec la liste des valeurs des var et 1 colonne avec le label de la var à expliquer 
		JavaRDD<Row> rdd3 = indexedData.toJavaRDD().repartition(2);
		
		JavaRDD<LabeledPoint> rdd4 = rdd3.map( new Function<Row, LabeledPoint>() {

			@Override
			public LabeledPoint call(Row iRow) throws Exception {
				double[] mlist = new double [varsX.length];
				int j = 0;
				for(int i = 0; i<varsX.length;i++) {
					if(varBool.contains(varsX[j])){
						//mlist[i]=iRow.getBoolean(i);
					}else {
						mlist[i]=iRow.getDouble(i);
					}
					j++;
					
				}
				Vector vect = Vectors.dense(mlist);
				LabeledPoint lp = new LabeledPoint(iRow.getDouble(varsX.length+1), vect);
				return lp;
				
			}

		});

		Dataset<Row> dataLp = spSession.createDataFrame(rdd4, LabeledPoint.class);
		spSession.cre
		//dataLp = dataCleansedDf;
		dataLp.show(5);

		// On split les données en données apprentissage / test
		Dataset<Row>[] splits = dataLp.randomSplit(new double[]{propApp, 1-propApp});
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];
		
		// puis on choisit la méthode
	    System.out.println("Veuillez choisir votre méthode (c pour cart, r pour random forest et s pour svm)");
	    System.out.println("Attention, le svm ne passe pas que sur une var à expliquer binaire et des var explicatives numériques !");
	    String choix = saisieUtilisateur.next();
	    
	    SparkMLMethod sm = null;
	    if(choix.equals("c")) {
	    	// Arbre de Classification
			sm = new SparkMLDecisionTree(siModel, trainingData, testData);
	    }
	    
	    if(choix.equals("r")) {
	    	// RandomForest
	    	sm = new SparkMLRandomForest(siModel, trainingData, testData);
	    }
	    
	    if(choix.equals("s")) {
	    	// SVM
	    	sm = new SparkMLSVM(siModel, trainingData, testData);
	    }
		
		sm.run();

	}
}
