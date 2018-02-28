package weka;

public abstract class WekaMethod {
	
	/** 
	 * classe abstraite qui sert de modèle aux trois méthodes de weka 
	 * */
	
	String strAdresseCsv;
	static String patharff;
	static String varY;
	String propApp;
	
	@SuppressWarnings("static-access")
	public WekaMethod(String strAdresseCsv, String strVariable, String propApp) throws Exception {
		
		this.strAdresseCsv = strAdresseCsv;
		this.varY = strVariable;
		this.propApp = propApp;
		
		// on change le csv en un fichier .arff
		CSV2Arff inst = CSV2Arff.getInstance();
		
		// on enlève l'extension .csv avec le -3, puis on rajoute la bonne extension en arf
		this.patharff = strAdresseCsv.substring(0, strAdresseCsv.length()-3)+"arff";
		
		// on applique la conversion csv -> arff
		inst.transfo(strAdresseCsv,this.patharff,this.varY);
	}
	
	public abstract void run() throws Exception;
	
}
