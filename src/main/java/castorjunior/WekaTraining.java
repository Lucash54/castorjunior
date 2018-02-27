package castorjunior;
 
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.*;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class WekaTraining {
	
	
	
public void training(String patharff, int columnY) throws Exception {
     // train classifier
     J48 cls = new J48();
     Instances data = new Instances(new BufferedReader(new FileReader(patharff)));
     
     Random rand = new Random(25);   // create seeded number generator
     Instances randData = new Instances(data);   // create copy of original data
     randData.randomize(rand);
     data.setClassIndex(columnY);

     
     Instances train = randData.trainCV(5, 0);
     Instances test = randData.testCV(5, 0);
     
     System.out.println(train.classIndex());
     System.out.println(train.numInstances());
     System.out.println(test.numInstances());     
     
     train.setClassIndex(columnY);
     test.setClassIndex(columnY);

     cls.buildClassifier(train);
     
     Evaluation eval = new Evaluation(train);
     eval.evaluateModel(cls, test);
     System.out.println(eval.toSummaryString("\nResults\n======\n", false));
     
     System.out.println(eval.pctCorrect());
     

     // display classifier
     final javax.swing.JFrame jf = 
       new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
     jf.setSize(500,400);
     jf.getContentPane().setLayout(new BorderLayout());
     TreeVisualizer tv = new TreeVisualizer(null,
         cls.graph(),
         new PlaceNode2());
     jf.getContentPane().add(tv, BorderLayout.CENTER);
     jf.addWindowListener(new java.awt.event.WindowAdapter() {
       public void windowClosing(java.awt.event.WindowEvent e) {
         jf.dispose();
       }
     });

     jf.setVisible(true);
     tv.fitToScreen();
   }





public void training(String patharff) throws Exception {
    // train classifier
	Instances data = new Instances(new BufferedReader(new FileReader(patharff)));
	training(patharff, data.numAttributes() - 1);
	
	}











}