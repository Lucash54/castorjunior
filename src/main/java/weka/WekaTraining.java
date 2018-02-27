package weka;
 
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
	
	/** à faire disparaitre dès qu'on aura les menus */
	
	public static void main(String[] args) throws Exception {
		CSV2Arff inst = CSV2Arff.getInstance();
		
		// On transforme son csv en arff (en modifiant la var à expliquer)
		inst.transfo("./src/pages.csv", "./src/pages.arff","nbPages");
		inst.transfo("./src/iris.csv", "./src/iris.arff","Species");
		
		// On lance le training 
		training("./src/pages.arff","nbPages");
		//training("./src/iris.arff","Species");
	}
	
	/** gère la classification par arbre CART
	 * en entrée :
	 * - le chemin du fichier .arff
	 * - le nom de la variable à expliquer dans le csv
	 * en sortie console:
	 * - le résumé du modèle CART utilisé, comprenant la précision du modèle
	 * - une JFrame contenant l'arbre
	 */
	
	public static void training(String patharff, String varY) throws Exception {
		
	     // J48 est la classe qui correspond à la classification des arbres CART
	     J48 cls = new J48();
	     
	     // on crée une instance, qui va lire le fichier .arff contenu dans l'adresse définie par patharff
	     Instances data = new Instances(new BufferedReader(new FileReader(patharff)));
	    
	     // puis on crée une génération de nombres aléatoires, qui serviront à délimiter 
	     // l'échantillon d'apprentissage et l'échantillon de test
	     
	     Random rand = new Random();
	     //On récupère le numéro de colonne de la variable à expliquer
	     int columnY = data.attribute(varY).index();
	     
	     // on crée une copie des données
	     Instances randData = new Instances(data); 
	     randData.randomize(rand);
	     data.setClassIndex(columnY);
	     
	     // on crée les éch. d'app et de test
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
	     final JFrame jf = new JFrame("Weka Classifier Tree : " + patharff);
	     
	     // On définit les options de la fenêtre, puis on insère le graphique relatif à l'arbre CART
	     jf.setSize(1800,1000);
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
}