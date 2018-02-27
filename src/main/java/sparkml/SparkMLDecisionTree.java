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

	public void run() {
		
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