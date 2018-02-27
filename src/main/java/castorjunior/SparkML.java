package castorjunior;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.google.common.collect.Lists;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2; 

public class SparkML {
	public static void main(String[] args) {

		JavaSparkContext sc = new JavaSparkContext("local[2]", 
			     "First Spark App"); 
			// we take the raw data in CSV format and convert it into a 
			// set of records of the form (user, product, price) 
			JavaRDD<String[]> data =   sc.textFile("data/UserPurchaseHistory.csv").map(s ->         s.split(","));
			sc.close();
	}
}
