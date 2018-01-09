package castorjunior;

import javax.script.*;
import org.renjin.script.*;

public class Rengine {

	  public static void main(String[] args) throws Exception {
		 
	    // create a script engine manager:
	    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    
	    // create a Renjin engine:
	    ScriptEngine engine = factory.getScriptEngine();

	    System.out.print(engine.eval("2+2"));
	    
	  }


}
