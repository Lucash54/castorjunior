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

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		System.out.println("Veuillez saisir le nombre d'arbres voulu :");
		String nbarbre = saisieUtilisateur.next();
		int nbTrees = Integer.parseInt(nbarbre);
		
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
