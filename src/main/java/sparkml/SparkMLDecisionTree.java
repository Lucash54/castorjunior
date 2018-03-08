package sparkml;

import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.col;


public class SparkMLDecisionTree extends SparkMLMethod{ 


	public SparkMLDecisionTree(StringIndexerModel siModel, Dataset<Row> trainingData, Dataset<Row> testData) {
		super(siModel,trainingData,testData);
	}

	public double run() {

		// Machine learning

		DecisionTreeClassifier dt = new DecisionTreeClassifier()
				.setLabelCol("label")
				.setFeaturesCol("features");

		// On récupère l'index des labels et on les remet en index original
		IndexToString labelConverter = new IndexToString()
				.setInputCol("label")
				.setOutputCol("labelStr")
				.setLabels(siModel.labels());

		IndexToString predConverter = new IndexToString()
				.setInputCol("prediction")
				.setOutputCol("predictionStr")
				.setLabels(siModel.labels());

		// Calcul du modèle d'arbre de classification
		DecisionTreeClassificationModel dtModel = dt.fit(trainingData);

		// Prédiction des données à partir du modèle calculé
		Dataset<Row> rawPredictions = dtModel.transform(testData);
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
		double accuracy = Math.round(evaluator.evaluate(predictions)*1000.0)/1000.0;
		System.out.println("Accuracy = " + accuracy * 100 + " %" );
		return accuracy;

	}


}