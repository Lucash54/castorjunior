package Main;

import rengine.Rengine;
import sparkml.SparkML;
import weka.WekaMain;

/**
 *Cette classe permet de créer une instance de la classe représentant une librairie  
 */

public class Library {
	protected static String strAdresse;
	protected static String strVariable;
	protected static double propApp;
	protected Library l;

	// Ce constructeur est utilisé quand on part de la mains d'une librarie
	public Library(String strAdresse, String strVariable, double propApp, String algo, int nbTrees) {
		this.strAdresse=strAdresse;
		this.strVariable=strVariable;
		this.propApp=propApp;
	}

	// Ce constructeur sert quand on part de la main de la classe Main
	public Library(String strAdresse, String strVariable, double propApp,String lib, String algo, int nbTrees) {
		this.strAdresse=strAdresse;
		this.strVariable=strVariable;
		this.propApp=propApp;
		if(lib.equals("w")) {
			// Weka
			this.l = new WekaMain(strAdresse, strVariable, propApp, algo, nbTrees);
		}

		if(lib.equals("r")) {
			// Rengine
			this.l =  new Rengine(strAdresse, strVariable, propApp, algo, nbTrees);
		}

		if(lib.equals("s")) {
			// SparkML
			this.l =  new SparkML(strAdresse, strVariable, propApp, algo, nbTrees);
		}
	}
	
	public double run() {
		return this.l.run();
	}

	
	// Les getter ont été utiles pour la partie sparkml
	public String getStrAdresse() {
		return strAdresse;
	}

	public String getStrVariable() {
		return strVariable;
	}

	public double getPropApp() {
		return propApp;
	}

	public Library getL() {
		return l;
	}
}
