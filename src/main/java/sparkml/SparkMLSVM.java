package sparkml;

import static org.apache.spark.sql.functions.col;
import java.util.Scanner;
import org.apache.spark.ml.classification.LinearSVC;
import org.apache.spark.ml.classification.LinearSVCModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class SparkMLSVM extends SparkMLMethod{
	
	public SparkMLSVM(StringIndexerModel siModel, Dataset<Row> trainingData, Dataset<Row> testData) {
		super(siModel,trainingData,testData);
	}
	
	public void run() {

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);
		
		System.out.println("Veuillez saisir le nombre d'itérations max :");
		String niter = saisieUtilisateur.next();
		int nbIter = Integer.parseInt(niter);
		
		System.out.println("Veuillez saisir le paramètre de régularisation :");
		String param = saisieUtilisateur.next();
		double lambda = Double.parseDouble(param);
		/*--------------------------------------------------------------------------
		Perform machine learning. 
		--------------------------------------------------------------------------*/	

		//Create the object
		// Train a DecisionTree model.
		LinearSVC svm = new LinearSVC()
				  .setLabelCol("label")
				  .setFeaturesCol("features")
				  .setMaxIter(nbIter)
				  .setRegParam(lambda);
		
		// Convert indexed labels back to original labels.
		IndexToString labelConverter = new IndexToString()
				  .setInputCol("label")
				  .setOutputCol("labelStr")
				  .setLabels(siModel.labels());
		
		IndexToString predConverter = new IndexToString()
				  .setInputCol("prediction")
				  .setOutputCol("predictionStr")
				  .setLabels(siModel.labels());

		LinearSVCModel svmm = svm.fit(trainingData);
		
		//Predict on test data
		Dataset<Row> rawPredictions = svmm.transform(testData);
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
