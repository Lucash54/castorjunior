package Main;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Console {

	private static Pattern p;

	public String strAdresse = "";
	public String strVariable = ".";
	public double propApp;
	public String lib = "";
	public String algo = "";
	public int nbTrees;

	public Console() {}

	public void inputMain() {
		inputLibrary();

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		// Renseigner la library
		p = Pattern.compile("^(w|r|s)$");
		while(!p.matcher(this.lib).matches()) {
			System.out.println("Veuillez saisir la librairie que vous souhaitez utiliser (w pour weka, r pour renjin ou s pour sparkml) :");
			this.lib = saisieUtilisateur.next();
		}

	}

	public void inputLibrary() {
		//On crée un scanner, pour que l'utilisateur puisse donner les renseignements nécessaires aux calculs

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		// Renseignerle chemin du csv 
		p = Pattern.compile(".\\.csv$");
		while(!p.matcher(this.strAdresse).find()) {
			System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/pages.csv ou ./src/iris.csv). La première colonne doit être un identifiant :");
			this.strAdresse = saisieUtilisateur.next();
		}
		
		// Renseigner le nom de la variable à expliquer 
		p = Pattern.compile("\\.");
		while(p.matcher(this.strVariable).find()) {
			System.out.println("Veuillez saisir le nom de la variable à expliquer (il faut qu'elle qu'elle respecte la casse et qu'elle ne contienne pas de point):");
			this.strVariable = saisieUtilisateur.next();
		}

		// Renseigner division échantillon (apprentissage/test)
		p = Pattern.compile("^(0\\.?\\d*)$");
		String pA="";
		while(!p.matcher(pA).matches()) {
			System.out.println("Veuillez saisir la proportion d'apprentissage (ex: pour 70% d'app et 30% de test, taper 0.7) :");
			pA = saisieUtilisateur.next();
		}
		this.propApp = Double.parseDouble(pA);

		// Renseigner l'algo
		p = Pattern.compile("^(c|r|s)$");
		while(!p.matcher(this.algo).find()) {
			System.out.println("Veuillez choisir votre méthode (c pour cart, r pour random forest et s pour svm)");
			this.algo = saisieUtilisateur.next();
		}

		// Renseigner le nombre d'arbres (si random forest)
		this.nbTrees = 1;
		if(algo.equals("r")) {
			String nbarbre = "";
			p = Pattern.compile("^[0-9]+$");
			while(!p.matcher(nbarbre).find()) {
				System.out.println("Veuillez saisir le nombre d'arbres voulu :");
				nbarbre = saisieUtilisateur.next();
			}
			this.nbTrees = Integer.parseInt(nbarbre);
		}
	}


}
