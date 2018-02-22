package castorjunior;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
 
import java.util.HashMap;
import java.util.Map;

/**
 * A simple Spark app in Java
 */
public class javap {
	public static void main(String[] args) {
	  
	 /* SparkSession spark = SparkSession.builder().config(null).getOrCreate();
		  
	  Dataset<Row>  dataFrame = spark.read().format("CSV").option("header","true").load("data/iris.csv");
	  System.out.println(dataFrame);*/
	  	
	  JavaSparkContext sc = new JavaSparkContext("local[2]", 
			     "First Spark App"); 
			// we take the raw data in CSV format and convert it into a 
			// set of records of the form (user, product, price) 
	  		JavaRDD<String> data1 = sc.textFile("data/iris.csv");
	  		String  head = data1.first();
	  		data1 = data1.filter(row -> row != head);
	  		String[] header = head.split(",");
	  		
	  		
	  		JavaRDD<String[]> data = data1.map(s ->     s.split(","));
	  		
	  		//org.apache.spark.ml.feature.LabeledPoint
	  		
	  		
	  		
	  		
	  		
	  		
	  		
	  		
	  		
	  		
	  		//SparkConf sparkConf = new SparkConf().setAppName("DecisionTreeExample");
	  		
	  		//JavaRDD[] tmp = data1.randomSplit(new double[]{0.6, 0.4});
	  		//JavaRDD trainingData = tmp[0]; // training set
	  		//JavaRDD testData = tmp[1]; // test set
	  		
	  		int numClasses = 3;
	  		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
	  		String impurity = "gini";
	  		int maxDepth = 5;
	  		int maxBins = 32;
	  		
	  		
	  		//DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
	        //          categoricalFeaturesInfo, impurity, maxDepth, maxBins);
	  		
	  		
			/*JavaRDD<String[]> data =   sc.textFile("data/iris.csv").map(s ->     s.split(","));
			System.out.println(header[1]);
			
			// let's count the number of purchases 
			long numPurchases = data.count(); 
			System.out.println(numPurchases);
			// let's count how many unique users made purchases 
			long uniqueUsers = data.map(strings ->  
			      strings[0]).distinct().count(); 
			System.out.println(uniqueUsers);
			// let's sum up our total revenue 
			/*Double totalRevenue = data.map(strings ->  
			      Double.parseDouble(strings[2])).reduce((Double v1,  
			Double v2) -> new Double(v1.doubleValue() + v2.doubleValue()));
			System.out.println(totalRevenue);*/
	  
  }
}