package castorjunior;

import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Normalizer;

public class Snippet {
public static void main(String[] args) {
	
	  JavaSparkContext sc = new JavaSparkContext("local[2]", 
			     "First Spark App"); 
			// we take the raw data in CSV format and convert it into a 
			// set of records of the form (user, product, price) 
	  		JavaRDD<String> data1 = sc.textFile("data/iris.csv");
	  		String  head = data1.first();
	  		data1 = data1.filter(row -> row != head);
	  		String[] header = head.split(",");
	  		
	  		
	  		JavaRDD<String[]> data = data1.map(s ->     s.split(","));

	"barais@irisa.fr";
	
	
	// convert every string to doubles
	  		JavaRDD<Double[]> dbl = data.map (m -> m.map(j->j.toDouble());
	// Here we create a Dense Vector and LabeledPoint. 0 Dense Vector is a
	// special kind of Vector than the scala regular Vector. So use Vectors, with an “s”
	// at the end. The values must be doubles. A Dense Vector cannot handle
	// blank values. If we had that those, we would use a Sparse Vector.
	//
	// The LabeledPoint is an object with the label and features. The label
	// is 0, 1, 2 or gas mileage and the features are the weight, year, etc.
	  		JavaRDD<LabeledPoint> parsedData = dbl.map (y -> 
	new LabeledPoint(y(0), Vectors.dense(y(1),y(2),y(3),y(4),y(5),y(6),y(7)))
	).cache();
	// numClasses means number of classifications. In this case it is
	// 0, 1, or 2
	int numClasses = 3;
	// Here we say that the 6th feature is categorical with 3 possible values.
	// It can be American, European, or Asian.
	// The rest of the values are continuous, meaning just numbers and not discrete
	// values.
	Map<Integer,Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
	categoricalFeaturesInfo.put(6,3);
	//Map(6 -> 3)
	String impurity = "gini";
	int maxDepth = 9;
	int maxBins = 7;
	// Now feed the data into the model.
	DecisionTreeModel model = DecisionTree.trainClassifier(parsedData, numClasses, categoricalFeaturesInfo , impurity, maxDepth, maxBins);
	// Print out the results
	System.out.println("Learned classification tree model:\n" + model.toDebugString());
}

}
