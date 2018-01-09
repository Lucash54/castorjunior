package castorjunior;

import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class SparkML {
	public static void main(String[] args) {
	

	// Load the data stored in LIBSVM format as a DataFrame.
	DataFrame data = sqlContext.read().format("libsvm").load("data/mllib/sample_libsvm_data.txt");

	// Index labels, adding metadata to the label column.
	// Fit on whole dataset to include all labels in index.
	StringIndexerModel labelIndexer = new StringIndexer()
	  .setInputCol("label")
	  .setOutputCol("indexedLabel")
	  .fit(data);

	// Automatically identify categorical features, and index them.
	VectorIndexerModel featureIndexer = new VectorIndexer()
	  .setInputCol("features")
	  .setOutputCol("indexedFeatures")
	  .setMaxCategories(4) // features with > 4 distinct values are treated as continuous
	  .fit(data);

	// Split the data into training and test sets (30% held out for testing)
	DataFrame[] splits = data.randomSplit(new double[]{0.7, 0.3});
	DataFrame trainingData = splits[0];
	DataFrame testData = splits[1];

	// Train a DecisionTree model.
	DecisionTreeClassifier dt = new DecisionTreeClassifier()
	  .setLabelCol("indexedLabel")
	  .setFeaturesCol("indexedFeatures");

	// Convert indexed labels back to original labels.
	IndexToString labelConverter = new IndexToString()
	  .setInputCol("prediction")
	  .setOutputCol("predictedLabel")
	  .setLabels(labelIndexer.labels());

	// Chain indexers and tree in a Pipeline
	Pipeline pipeline = new Pipeline()
	  .setStages(new PipelineStage[]{labelIndexer, featureIndexer, dt, labelConverter});

	// Train model.  This also runs the indexers.
	PipelineModel model = pipeline.fit(trainingData);

	// Make predictions.
	DataFrame predictions = model.transform(testData);

	// Select example rows to display.
	predictions.select("predictedLabel", "label", "features").show(5);

	// Select (prediction, true label) and compute test error
	MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
	  .setLabelCol("indexedLabel")
	  .setPredictionCol("prediction")
	  .setMetricName("precision");
	double accuracy = evaluator.evaluate(predictions);
	System.out.println("Test Error = " + (1.0 - accuracy));

	DecisionTreeClassificationModel treeModel =
	  (DecisionTreeClassificationModel) (model.stages()[2]);
	System.out.println("Learned classification tree model:\n" + treeModel.toDebugString());
	}
}
