package sparkml;

import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public abstract class SparkMLMethod {
	StringIndexerModel siModel;
	Dataset<Row> trainingData;
	Dataset<Row> testData;
	
	public SparkMLMethod(StringIndexerModel siModel, Dataset<Row> trainingData, Dataset<Row> testData) {
		this.siModel = siModel;
		this.trainingData = trainingData;
		this.testData = testData;
	}
	
	public abstract double run();
}
