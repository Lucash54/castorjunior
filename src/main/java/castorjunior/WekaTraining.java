package castorjunior;
 
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import javax.swing.JFrame;

import weka.classifiers.*;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class WekaTraining {
	
	/** lance le training */
	public static void main(String[] args) throws Exception {
		training("./src/iris.arff");
		training("./src/csv.arff");
	}
	
	/** gère la classification par arbre CART
	 * en entrée :
	 * - le chemin du fichier
	 * 
	 */
	
	public static void training(String patharff, int columnY) throws Exception {
		
	     // J48 est la classe qui correspond à la classification des arbres CART
	     J48 cls = new J48();
	     
	     // on crée une instances, qui va lire le fichier .arff contenu dans l'adresse définie par patharff
	     Instances data = new Instances(new BufferedReader(new FileReader(patharff)));
	     
	     // puis on crée une génération de nombres aléatoires, qui serviront à délimiter 
	     // l'échantillon d'apprentissage et l'échantillon de test
	     Random rand = new Random();
	     
	     // on crée une copie des données
	     Instances randData = new Instances(data); 
	     randData.randomize(rand);
	     data.setClassIndex(columnY);
	
	     // on crée les éch d'app et de test
	     Instances train = randData.trainCV(5, 0);
	     Instances test = randData.testCV(5, 0);
	     
	     System.out.println("L'ensemble d'apprentissage dénombre " + train.numInstances() + " individus");
	     System.out.println("L'ensemble de test dénombre " + test.numInstances() + " individus");     
	     
	     // on donne la variable à expliquer pour les ensembles d'apprentissage et de validation
	     train.setClassIndex(columnY);
	     test.setClassIndex(columnY);
	
	     // on démarre le machine learning sur l'ensemble d'apprentissage
	     cls.buildClassifier(train);
	     
	     // on évalue la performance du modèle sur les données de test
	     Evaluation eval = new Evaluation(train);
	     eval.evaluateModel(cls, test);
	     
	     // on affiche le résumé du modèle
	     System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	     
	     // Puis la précision
	     System.out.println(eval.pctCorrect());
	     
	
	     // Puis on cherche à afficher l'arbre obtenu dans une JFrame
	     final JFrame jf = new JFrame("Weka Classifier Tree : "+ patharff);
	     
	     // On définit les options de la fenêtre, puis on insère le graphique relatif à l'arbre CART
	     jf.setSize(500,400);
	     jf.getContentPane().setLayout(new BorderLayout());
	     TreeVisualizer tv = new TreeVisualizer(null,cls.graph(),new PlaceNode2());
	     jf.getContentPane().add(tv, BorderLayout.CENTER);
	     
	     // pour pouvoir fermer la fenêtre
	     jf.addWindowListener(new java.awt.event.WindowAdapter() {
	       public void windowClosing(java.awt.event.WindowEvent e) {
	         jf.dispose();
	       }
	     });
	     
	     // enfin, on la rend visible
	     jf.setVisible(true);
	     tv.fitToScreen();
	  }





	public static void training(String patharff) throws Exception {
		
		Instances data = new Instances(new BufferedReader(new FileReader(patharff)));
		training(patharff, data.numAttributes() - 1);
		
	}











}