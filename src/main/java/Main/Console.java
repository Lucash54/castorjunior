package Main;

import java.util.Scanner;

public class Console {

	public String strAdresse;
	public String strVariable;
	public double propApp;
	public String lib;
	public String algo;
	public int nbTrees = 1;

	public Console() {}

	public void inputMain() {
		inputLibrary();
		
		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		System.out.println("Veuillez saisir la librairie que vous souhaitez utiliser (w pour weka, r pour renjin ou s pour sparkml) :");
		this.lib = saisieUtilisateur.next();

	}

	public void inputLibrary() {
		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, le nom de la variable explicative,
		//ainsi que le pourcentage d'apprentissage 

		@SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);

		System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/pages.csv ou ./src/iris.csv, ou n'importe quelle adresse contenant un csv) :");
		this.strAdresse = saisieUtilisateur.next();

		System.out.println("Veuillez saisir le nom de la variable à expliquer (il faut qu'elle existe dans le csv et qu'elle respecte la casse, sinon le code ne marchera pas):");
		this.strVariable = saisieUtilisateur.next();

		System.out.println("Veuillez saisir la proportion d'apprentissage (ex: pour 70% d'app et 30% de test, taper 0.7) :");
		String pA = saisieUtilisateur.next();
		this.propApp = Double.parseDouble(pA);

		System.out.println("Veuillez choisir votre méthode (c pour cart, r pour random forest et s pour svm)");
		this.algo = saisieUtilisateur.next();

		this.nbTrees = 1;
		if(algo.equals("r")) {
			System.out.println("Veuillez saisir le nombre d'arbres voulu :");
			String nbarbre = saisieUtilisateur.next();
			this.nbTrees = Integer.parseInt(nbarbre);
		}
	}


}
