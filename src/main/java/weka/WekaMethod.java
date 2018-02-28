package weka;

public abstract class WekaMethod {
	
	// classe abstraite qui sert de modèle aux trois méthodes de weka
	
	String strAdresseCsv;
	static String patharff;
	static String varY;
	String propApp;
	
	@SuppressWarnings("static-access")
	public WekaMethod(String strAdresseCsv, String strVariable, String propApp) throws Exception {
		this.strAdresseCsv = strAdresseCsv;
		this.varY = strVariable;
		this.propApp = propApp;
		CSV2Arff inst = CSV2Arff.getInstance();
		this.patharff = strAdresseCsv.substring(0, strAdresseCsv.length()-3)+"arff";
		inst.transfo(strAdresseCsv,this.patharff,this.varY);
	}
	
}
