package weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

public class WekaSVM extends WekaMethod {

	/** constructeur qui reprend les méthodes de WekaMethod
	 *  et qui construit le .arff à partir de l'adresse du csv
	 * @param strAdresseCsv
	 * @param strVariable
	 * @param propApp
	 * @throws Exception
	 */

	public WekaSVM(String strAdresseCsv, String strVariable, double propApp) throws Exception {
		super(strAdresseCsv, strVariable, propApp);
	}

	/** gère la classification par svm
	 * en entrée :
	 * - le chemin du fichier .arff
	 * - le nom de la variable à expliquer dans le csv
	 * en sortie console:
	 * - le résumé du modèle utilisé, comprenant la précision du modèle
	 * @return 
	 */

	public double run() throws Exception {

		// SMO est la classe qui gère les support vector machine
		SMO cls = new SMO();

		// on crée une instance, qui va lire le fichier .arff contenu dans l'adresse définie par patharff
		Instances data = new Instances(new BufferedReader(new FileReader(patharff)));

		// puis on crée une génération de nombres aléatoires, qui serviront à délimiter 
		// l'échantillon d'apprentissage et l'échantillon de test

		Random rand = new Random();
		//On récupère le numéro de colonne de la variable à expliquer
		int columnY = data.attribute(varY).index();

		// on crée une copie des données pour les mélanger
		Instances randData = new Instances(data); 
		randData.randomize(rand);
		data.setClassIndex(columnY);

		// on crée les éch. d'app et de test
		double prop = propApp;
		int trainSize = (int) Math.floor(randData.numInstances()*prop);
		int testSize = randData.numInstances() - trainSize;
		Instances train = new Instances(randData, 0, trainSize);
		Instances test = new Instances(randData, trainSize, testSize);

		System.out.println("\n" + patharff + "\n");

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
		return accuracy;

	}
}
