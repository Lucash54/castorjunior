package rengine;

import javax.script.ScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import Main.Console;
import Main.Library;

public class Rengine extends Library{
	
	protected RengineMethod rmeth;
	protected ScriptEngine engine;
	
	// Lorsque l'on crée la classe Rengine, on initialise une instance de RengineMethod avec les données de l'utilisateur,
	// il ne reste plus qu'à utilisé la méthode run de cette instance. 
	public Rengine(String strAdresse, String strVariable, double propApp, String algo, int nbTrees) {
		super(strAdresse, strVariable, propApp, algo, nbTrees);
	    
		// crée un script Rengine manager:
	    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    
	    // crée un Rengine:
		engine = factory.getScriptEngine();
		
		// Selon l'algo que l'utilisateur a choisit, on va dans la classe fille de RengineMethod correspondante
		if(algo.equals("c")) {
			// Arbre de Classification
	    	System.out.println("Cart choisi!");
	    	rmeth = new RengineCART(strAdresse, strVariable, propApp, engine);
	    }
	    
	    if(algo.equals("r")) {
			// RandomForest
	    	System.out.println("RandomForest choisi!");
	    	rmeth = new RengineRandomForest(strAdresse, strVariable, propApp, engine, nbTrees);
	    }
	    
	    if(algo.equals("s")) {
			// SVM
	    	System.out.println("SVM choisi!");
	    	rmeth = new RengineSVM(strAdresse, strVariable, propApp, engine);
	    }
	}

	public static void main(String[] args) {
		System.out.println("Vous utilisez la librairie Rengine.");
		Console console = new Console();
		console.inputLibrary();
		
		
		Rengine r = new Rengine(console.strAdresse, console.strVariable, console.propApp, console.algo, console.nbTrees);
		r.run();
	   
	}

	@Override
	public double run() {
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
	    
	  	    double accuracy = rmeth.run();
	  	    return accuracy;
	}

}
