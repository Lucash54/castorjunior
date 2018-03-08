package weka;

import Main.Console;
import Main.Library;

public class WekaMain extends Library{
	protected WekaMethod wmeth;

	public WekaMain(String strAdresse, String strVariable, double propApp, String algo, int nbTrees) {
		super(strAdresse, strVariable, propApp, algo, nbTrees);
		try {
			if(algo.equals("c")) {
				System.out.println("Cart choisi!");
				wmeth = new WekaCART(strAdresse, strVariable, propApp);
			}

			if(algo.equals("r")) {
				System.out.println("RandomForest choisi!");
				wmeth = new WekaRandomForest(strAdresse, strVariable, propApp, nbTrees);
			}

			if(algo.equals("s")) {
				System.out.println("SVM choisi!");
				wmeth = new WekaSVM(strAdresse, strVariable, propApp);
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Console console = new Console();
		console.inputLibrary();
		
		WekaMain wm = new WekaMain(console.strAdresse, console.strVariable, console.propApp, console.algo, console.nbTrees);
		wm.run();
	}

	@Override
	public double run() {
		double aux =0;
		try {
			aux= wmeth.run();		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aux;
	}

}