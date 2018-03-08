package sparkml;

import Main.Console;
import Main.Library;

/**
 * Cette classe SparkML permet de calculer, grâce à 3 méthodes (arbre de classification, random forest et svm),
 * l'accuracy du modèle sur les données.
 *
 * Consignes : Le fichier est un csv. Les variables explicatives peuvent être numériques ou booléennes.
 * La première colonne est un identifiant (inutile pour les calculs).
 * Pour le svm, la variable à expliquer doit être binaire.
 * Les noms de variables ne doivent pas contenir de point.
 */



public class SparkML extends Library{

	protected String algo;
	protected int nbTrees;
	
	public SparkML(String strAdresse, String strVariable, double propApp, String algo, int nbTrees) {
		super(strAdresse, strVariable, propApp, algo, nbTrees);
		this.algo = algo;
		this.nbTrees = nbTrees;
	}


	public String getAlgo() {
		return algo;
	}


	public int getNbTrees() {
		return nbTrees;
	}


	public static void main(String [] args) {
		Console console = new Console();
		console.inputLibrary();
		
		SparkML sml = new SparkML(console.strAdresse, console.strVariable, console.propApp, console.algo, console.nbTrees);
		RunnableSparkML t = new RunnableSparkML();
		t.runSpark(sml);
	}

	protected static int y = 0;

	@Override
	public double run() {
		RunnableSparkML t = new RunnableSparkML();
		return t.runSpark(this);
	}

}
