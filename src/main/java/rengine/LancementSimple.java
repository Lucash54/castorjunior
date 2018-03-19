package rengine;

import javax.script.ScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;

public class LancementSimple {
		
		public static void main(String[] args) {
			
		    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
			ScriptEngine engine = factory.getScriptEngine();
			String strAdresse = "./src/iris.csv";
			String strVariable = "Species";
			String propApp = "0.8";
			RengineSVM rmeth = new RengineSVM(strAdresse, strVariable, Double.parseDouble(propApp), engine);
			
			
			 try{
			    	engine.eval("csv <- read.csv("+"\""+strAdresse+"\")");
			    	engine.eval("nomVar <- "+"\""+strVariable+"\"");
			    	engine.eval("propApp <- as.double("+propApp+")");
		    	}
			    
		    	catch (Exception e){
		    		System.out.println(e.toString());
		    	}
			 rmeth.run();
		}

}
