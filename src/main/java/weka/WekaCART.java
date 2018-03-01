package weka;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.renjin.gnur.api.Graphics;

import weka.classifiers.*;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.graphvisualizer.GraphVisualizer;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeDisplayListener;
import weka.gui.treevisualizer.TreeVisualizer;

@SuppressWarnings("unused")
public class WekaCART extends WekaMethod {
	
	/** constructeur qui reprend les méthodes de WekaMethod
	 *  et qui construit le .arff à partir de l'adresse du csv
	 * @param strAdresseCsv
	 * @param strVariable
	 * @param propApp
	 * @throws Exception
	 */
	
	public WekaCART(String strAdresseCsv, String strVariable, String propApp) throws Exception {
		super(strAdresseCsv, strVariable, propApp);
	}
	
	/** gère la classification par arbre CART
	 * en entrée :
	 * - le chemin du fichier .arff
	 * - le nom de la variable à expliquer dans le csv
	 * en sortie console:
	 * - le résumé du modèle CART utilisé, comprenant la précision du modèle
	 * - une JFrame contenant l'arbre
	 */
	
	public void run() throws Exception {
		
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
	     double prop = Double.parseDouble(propApp);
	     int trainSize = (int) Math.floor(randData.numInstances()*prop);
	     int testSize = randData.numInstances() - trainSize;
	     Instances train = new Instances(randData, 0, trainSize);
	     Instances test = new Instances(randData, trainSize, testSize);
	     
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
	     System.out.println(cls.graph());
	     TreeVisualizer tv = new TreeVisualizer(null,cls.graph(),new PlaceNode2());
	     GraphVisualizer graphe = new GraphVisualizer();
	     Image imageArbre = tv.createImage(1000, 1000);
	     
	     JPanel jp = new JPanel();
	     jp.setLayout(new BorderLayout());
	     jp.setSize(1000, 1000);
	     jp.add(tv, BorderLayout.CENTER);
	     
	     BufferedImage bi = new BufferedImage(jp.getSize().height, jp.getSize().width, BufferedImage.TYPE_INT_ARGB); 
	     Graphics2D g = bi.createGraphics();
	     jp.setVisible(true);
	     jp.paint(g);
	     tv.printAll(g);
	     g.dispose();
	     try{
	    	 ImageIO.write(bi,"png",new File("./src/test.png"));
	     }
	     catch (Exception e) {}
		 
		 
		 
		 
		 
		 
		 
		 
		// Puis on cherche à afficher l'arbre obtenu dans une JFrame
	     final JFrame jf = new JFrame("Weka Classifier Tree : " + patharff);
	    
	     // On définit les options de la fenêtre, puis on insère le graphique relatif à l'arbre CART
	     jf.setSize(1800,1000);
	     jf.setContentPane(jp);
	     
	     // pour pouvoir fermer la fenêtre
	     jf.addWindowListener(new java.awt.event.WindowAdapter() {
	       public void windowClosing(java.awt.event.WindowEvent e) {
	         jf.dispose();
	       }
	     });
	     
	     // enfin, on la rend visible
	     //jf.setVisible(true);
	     //tv.fitToScreen();
	  }
	
}