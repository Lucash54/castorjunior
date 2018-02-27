package sparkml;

import static org.apache.spark.sql.functions.col;
import java.util.Scanner;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class SparkMLRandomForest extends SparkMLMethod{
	
	
	public SparkMLRandomForest(StringIndexerModel siModel, Dataset<Row> trainingData, Dataset<Row> testData) {
		super(siModel,trainingData,testData);
	}
	
	public void run() {
		
		// Scanner pour les infos complémentaires
		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		System.out.println("Veuillez saisir le nombre d'arbres voulu :");
		String nbarbre = saisieUtilisateur.next();
		int nbTrees = Integer.parseInt(nbarbre);
		
		// Machine learning
		
		RandomForestClassifier rf = new RandomForestClassifier()
				  .setLabelCol("label")
				  .setFeaturesCol("features")
				  .setNumTrees(nbTrees);
		
		// On récupère l'index des labels et on les remet en index original
		IndexToString labelConverter = new IndexToString()
				  .setInputCol("label")
				  .setOutputCol("labelStr")
				  .setLabels(siModel.labels());
		
		IndexToString predConverter = new IndexToString()
				  .setInputCol("prediction")
				  .setOutputCol("predictionStr")
				  .setLabels(siModel.labels());

		// Calcul du modèle de random forest
		RandomForestClassificationModel rfm = rf.fit(trainingData);
		
		// Prédiction des données à partir du modèle calculé
		Dataset<Row> rawPredictions = rfm.transform(testData);
		Dataset<Row> predictions = predConverter.transform(
									labelConverter.transform(rawPredictions));
		
		// Affichage des 5 premières lignes
		System.out.println("Result sample :");
		predictions.select("labelStr", "predictionStr", "features").show(5);

		// Affichage de la matrice de confusion
		System.out.println("Confusion Matrix :");
		predictions.groupBy(col("labelStr"), col("predictionStr")).count().show();
		
		// Calcul de l'accuracy
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
				  .setLabelCol("label")
				  .setPredictionCol("prediction")
				  .setMetricName("accuracy");
				double accuracy = evaluator.evaluate(predictions);
				System.out.println("Accuracy = " + Math.round( accuracy * 100) + " %" );
				
	}
}
