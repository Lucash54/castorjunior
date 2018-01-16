package castorjunior;


import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;





public class wazaaaaaaa {
	
	
	public static void main(String[] ags) throws Exception {
		/*FileReader oui = new FileReader("data/iris2.arff");
		BufferedReader reader = new BufferedReader(oui);

        //Get the data
        Instances data = new Instances(reader);
        reader.close();
        */

        System.out.println("sdfsf");		

		
        DataSource source = new DataSource("data/iris.csv");
        Instances data = source.getDataSet();

        //Setting class attribute 
        data.setClassIndex(data.numAttributes() - 1);

        //Make tree
        J48 tree = new J48();
        String[] options = new String[1];
        options[0] = "-U"; 
        tree.setOptions(options);
        tree.buildClassifier(data);

        //Print tree
        System.out.println(tree);
		
        System.out.println(tree.graph());
        System.out.println(tree.toString());
        System.out.println("sdfsf");		
	}
    
    

}
