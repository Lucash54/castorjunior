package castorjunior;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.script.*;
import org.renjin.script.*;

public class Rengine {

	 @SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		
		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv, et le nom de la variable explicative
		
		Scanner saisieUtilisateur = new Scanner(System.in); 
		
		//On crée un scanner, pour que l'utilisateur puisse donner l'adresse du csv,
		// le nom de la variable explicative
		// le nombre d'arbres
		
		System.out.println("Veuillez saisir l'adresse du csv :");
		//String strAdresse = saisieUtilisateur.next();
		//J'ai remplacé par une adresse existante pour ne pas taper à chaque fois, il faudra décommenter à la fin
		String strAdresse = "/home/luc/git/castorjunior/src/pages.csv";
		
		System.out.println("Veuillez saisir le nom de la variable à expliquer :");
		//String strVariable = saisieUtilisateur.next();
		//J'ai remplacé par la variable, il faudra décommenter à la fin
		String strVariable = "nbPages";
		
		System.out.println("Veuillez saisir le nombre d'arbres utilisés :");
		//String nbArbre = saisieUtilisateur.next();
		//J'ai remplacé par la variable, il faudra décommenter à la fin
		String nbArbre = "800";
		
		
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
	    	
	    	// puis le nombre d'arbres utilisés
	    	engine.eval("nbArbre <- as.numeric("+nbArbre+")");
	    	
	    	// puis on crée  un lecteur de fichier r pour lire le script du randomForest ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/script.R");
	    	InputStreamReader lecture = new InputStreamReader(flux);
	    	BufferedReader buff = new BufferedReader(lecture);
	    	String ligne;
	    	
	    	// exemple des pages : /home/luc/git/castorjunior/src/pages.csv
	    	// nbPages comme nom de variable
	    	// exemple des iris : /home/luc/git/castorjunior/src/iris.csv
	    	// Species comme nom de variable
	    	
	    	// tant que le fichier n'est pas entièrement lu, càd qu'on arrive pas à la dernière ligne
	    	
	    	while ((ligne=buff.readLine())!=null){
	    		System.out.println(engine.eval(ligne));
	    		// on évalue le code R associé à la ligne lue
	    	}
	    	buff.close(); 
    	}		
	    
    	catch (Exception e){
    		System.out.println(e.toString());
    	}
	    
	  }

}
