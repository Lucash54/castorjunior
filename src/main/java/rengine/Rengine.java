package rengine;

import java.util.Scanner;

import javax.script.ScriptEngine;

import org.renjin.script.RenjinScriptEngineFactory;

public class Rengine {
	
	//à terme, ça ne sera pas une main, mais plutôt un appel via un menu plus grand, qui pourra lancer les trois librairies
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		// On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, et le nom de la variable explicative
		
		Scanner saisieUtilisateur = new Scanner(System.in);
		
		// On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, 
		// le nom de la variable explicative,
		// et la proportion de bien classés
		
		System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/iris.csv ou ./src/pages.csv, ou n'importe quelle adresse contenant un csv) :");
		String strAdresse = saisieUtilisateur.next();
		
		System.out.println("Veuillez saisir le nom de la variable à expliquer (sur les exemples d'au-dessus, Species et nbPages):");
		String strVariable = saisieUtilisateur.next();
		
		System.out.println("Veuillez saisir la proportion d'apprentissage (ex: pour 70% d'app et 30% de test, taper 0.7) :");
		String propApp = saisieUtilisateur.next();
		
	    // crée un script Rengine manager:
	    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    
	    // crée un Rengine:
		ScriptEngine engine = factory.getScriptEngine();
		

	    try{
	    	
	    	// on importe d'abord le nom de l'adresse mémoire du csv sous R
	    	engine.eval("csv <- read.csv("+"\""+strAdresse+"\")");
	    	System.out.println("Import des données réussi!");
	    	
	    	// puis le nom de la variable
	    	engine.eval("nomVar <- "+"\""+strVariable+"\"");
	    	
	    	// et enfin la proportion d'apprentissage
	    	engine.eval("propApp <- as.double("+propApp+")");
	    	
    	}		
	    
    	catch (Exception e){
    		System.out.println(e.toString());
    	}
	    
	    //puis on choisit la méthode
	    System.out.println("Veuillez choisir votre méthode (c pour cart, r pour random forest et s pour svm)");
	    String choix = saisieUtilisateur.next();
	    
	    RengineMethod rm = null;
	    if(choix.equals("c")) {
	    	System.out.println("Cart choisi!");
	    	rm = new RengineCART(strAdresse, strVariable, propApp, engine);
	    }
	    
	    if(choix.equals("r")) {
	    	System.out.println("RanfomForest choisi!");
	    	rm = new RengineRandomForest(strAdresse, strVariable, propApp, engine);
	    }
	    
	    if(choix.equals("s")) {
	    	System.out.println("SVM choisi!");
	    	rm = new RengineSVM(strAdresse, strVariable, propApp, engine);
	    }
	    rm.run();
	}

}
