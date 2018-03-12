package rengine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.script.ScriptEngine;

public class RengineRandomForest extends RengineMethod {

	int nbArbre;
	
	public RengineRandomForest(String strAdresse, String strVariable, double propApp, ScriptEngine engine, int nbTrees) {
		super(strAdresse, strVariable, propApp, engine);
		this.nbArbre=nbTrees;
		
	}

	public double run() {
		double accuracy = 0;
	    try{
	    	
	    	// puis le nombre d'arbres utilisés
	    	engine.eval("nbArbre <- "+nbArbre);

	    	// puis on crée  un lecteur de fichier r pour lire le script du randomForest ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/randomForest.R");
	    	InputStreamReader lecture = new InputStreamReader(flux);
	    	BufferedReader buff = new BufferedReader(lecture);
	    	String ligne;
	    	
	    	// tant que le fichier n'est pas entièrement lu, càd qu'on arrive pas à la dernière ligne
	    	String output="";
	    	StringWriter outputWriter=null;
	    	while ((ligne=buff.readLine())!=null){
	    		outputWriter = new StringWriter();
		    	engine.getContext().setWriter(outputWriter);
	    		System.out.println(engine.eval(ligne));
	    		output = outputWriter.toString();
	    		// on évalue le code R associé à la ligne lue
	    	}
	    	
	    	// Permet de changer la virgule en point pour transformer le string en double
	    	String output2 ="";
	    	if(output.contains(",")) {
	    		output2 = output.split(",")[0]+"."+output.split(",")[1];
	    	}else {
	    		output2 = output;
	    	}
	    	accuracy = Double.parseDouble(output2.split(" ")[1]);
	    	accuracy = Math.round(accuracy*1000.0)/1000.0;
	    	
	    	// une fois le code exécuté, on ferme le buffer. Normalement l'accuracy est la dernière ligne affichée
	    	buff.close();
    	}		
	    
    	catch (Exception e){
    		System.out.println(e.toString());
    	}
		return accuracy;
	    
	  }

}
