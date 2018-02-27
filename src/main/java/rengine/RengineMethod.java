package rengine;

import javax.script.ScriptEngine;


public abstract class RengineMethod {
	
	// classe abstraite qui sert de modèle aux trois méthodes de Rengine
	
	String strAdresse;
	String strVariable;
	String propApp;
	ScriptEngine engine;
	
	public RengineMethod(String strAdresse, String strVariable, String propApp, ScriptEngine engine) {
		this.strAdresse = strAdresse;
		this.strVariable = strVariable;
		this.propApp = propApp;
		this.engine = engine;
	}
	
	public abstract void run();
	
}
