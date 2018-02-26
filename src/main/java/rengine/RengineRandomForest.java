package rengine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.script.*;

public class RengineRandomForest extends RengineMethod {

	@SuppressWarnings("resource")
	public void run(String strAdresse, String strVariable, String propApp, ScriptEngine engine) {
		
		// on crée un scanner, pour que l'utilisateur puisse donner le nombre d'arbres
		
		Scanner saisieUtilisateur = new Scanner(System.in);
		
		System.out.println("Veuillez saisir le nombre d'arbres utilisés (par ex. 1000) :");
		String nbArbre = saisieUtilisateur.next();

	    try{
	    	
	    	// puis le nombre d'arbres utilisés
	    	engine.eval("nbArbre <- as.numeric("+nbArbre+")");

	    	// puis on crée  un lecteur de fichier r pour lire le script du randomForest ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/randomForest.R");
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
