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

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizEngine;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import guru.nidi.graphviz.model.Graph;
import web.MyServlet;
import weka.classifiers.*;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.graphvisualizer.GraphVisualizer;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeDisplayListener;
import weka.gui.treevisualizer.TreeVisualizer;

import static guru.nidi.graphviz.model.Factory.*;

@SuppressWarnings("unused")
public class WekaCART extends WekaMethod {
	
	/** constructeur qui reprend les méthodes de WekaMethod
	 *  et qui construit le .arff à partir de l'adresse du csv
	 * @param strAdresseCsv
	 * @param strVariable
	 * @param propApp
	 * @throws Exception
	 */
	
	public WekaCART(String strAdresseCsv, String strVariable, double propApp) throws Exception {
		super(strAdresseCsv, strVariable, propApp);
	}
	
	/** gère la classification par arbre CART
	 * en entrée :
	 * - le chemin du fichier .arff
	 * - le nom de la variable à expliquer dans le csv
	 * en sortie console:
	 * - le résumé du modèle CART utilisé, comprenant la précision du modèle
	 * - une JFrame contenant l'arbre
	 * @return 
	 */
	
	public double run() throws Exception {
		
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
	     double prop = propApp;
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
	     double accuracy = Math.round(eval.pctCorrect()*10.0)/1000.0;
	     System.out.println("Accuracy : "+ accuracy);
	     
	     TreeVisualizer tv = new TreeVisualizer(null,cls.graph(),new PlaceNode2());
	     /* Enregistrement du graphe*/
	     String nomGraphe = strAdresseCsv.substring(0, strAdresseCsv.length()-4);
	     Graphviz.useEngine(new GraphvizJdkEngine());
	     File file = new File(nomGraphe + MyServlet.idAlgo+".png");
	     Graphviz.fromString(cls.graph()).width(500).height(500).render(Format.PNG).toFile(new File(nomGraphe +MyServlet.idAlgo+".png"));
		return accuracy;
	  }
	
}