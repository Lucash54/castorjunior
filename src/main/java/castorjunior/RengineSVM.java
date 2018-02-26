package castorjunior;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.script.ScriptEngine;

import org.renjin.script.RenjinScriptEngineFactory;

public class RengineSVM {


	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, et le nom de la variable explicative
		
		Scanner saisieUtilisateur = new Scanner(System.in);
		
		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv,
		// le nom de la variable explicative
		
		System.out.println("Veuillez saisir l'adresse du csv (par ex. ./src/pages.csv ou ./src/iris.csv, ou n'importe quelle adresse contenant un csv) :");
		// à décommenter quand les tests seront finis
		//String strAdresse = saisieUtilisateur.next();
		String strAdresse = "./src/pages.csv";
		
		System.out.println("Veuillez saisir le nom de la variable à expliquer (il faut qu'elle existe dans le csv et qu'elle respecte la casse, sinon le code ne marchera pas):");
		// à décommenter quand les tests seront finis
		//String strVariable = saisieUtilisateur.next();
		String strVariable = "nbPages";
		
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
	    	
	    	// puis on crée  un lecteur de fichier r pour lire le script du randomForest ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/svm.R");
	    	InputStreamReader lecture = new InputStreamReader(flux);
	    	BufferedReader buff = new BufferedReader(lecture);
	    	String ligne;
	    	
	    	// tant que le fichier n'est pas entièrement lu, càd qu'on arrive pas à la dernière ligne
	    	
	    	while ((ligne=buff.readLine())!=null){
	    		System.out.println(engine.eval(ligne));
	    		// on évalue le code R associé à la ligne lue
	    	}
	    	// une fois le code exécuté, on ferme le buffer. Normalement l'accuracy est la dernière ligne affichée
	    	buff.close();
    	}		
	    
    	catch (Exception e){
    		System.out.println(e.toString());
    	}
	    
	  }

}
