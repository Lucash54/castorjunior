package castorjunior;

import javax.script.ScriptEngine;


public abstract class RengineMethod {
	
	// classe abstraite qui sert de modèle aux trois méthodes de Rengine
	
	static String strAdresse="";
	static String strVariable="";
	static String propApp="";
	
	public abstract void run(String strAdresse, String strVariable, String propApp, ScriptEngine engine);
	
}
