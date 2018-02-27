package rengine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;

public class RengineSVM extends RengineMethod {

	public RengineSVM(String strAdresse, String strVariable, String propApp, ScriptEngine engine) {
		super(strAdresse, strVariable, propApp, engine);
	}

	public void run() {

	    try{
	    	
	    	// on crée  un lecteur de fichier r pour lire le script du support vector machine ligne à ligne 
	    	
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