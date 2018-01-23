package castorjunior;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.*;
import org.renjin.script.*;

public class Rengine {

	  @SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		
	    // create a script engine manager:
	    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    
	    // create a Renjin engine:
		ScriptEngine engine = factory.getScriptEngine();

	    
	    try{	    		    	
	    	// on crée d'abord un lecteur de fichier r pour lire le fichier ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/script.R"); 
	    	InputStreamReader lecture = new InputStreamReader(flux);
	    	BufferedReader buff = new BufferedReader(lecture);
	    	String ligne;
	    	
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
