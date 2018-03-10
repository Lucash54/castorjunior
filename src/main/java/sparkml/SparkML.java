package sparkml;

import Main.Console;
import Main.Library;

/**
 * Il a fallut séparer cette classe en SparkML et RunnableSparkML, à cause de problème avec la sériabilisation de certaines méthodes.
 * Ici, on crée une instance de SparkML avec les données de l'utilisateur et on fait les calculs dans RunnableSparkML
 */

public class SparkML extends Library{

	protected String algo;
	protected int nbTrees;
	
	public SparkML(String strAdresse, String strVariable, double propApp, String algo, int nbTrees) {
		super(strAdresse, strVariable, propApp, algo, nbTrees);
		this.algo = algo;
		this.nbTrees = nbTrees;
	}


	public String getAlgo() {
		return algo;
	}


	public int getNbTrees() {
		return nbTrees;
	}


	public static void main(String [] args) {
		System.out.println("Vous utilisez la librairie SparkML.");
		Console console = new Console();
		console.inputLibrary();
		
		SparkML sml = new SparkML(console.strAdresse, console.strVariable, console.propApp, console.algo, console.nbTrees);
		RunnableSparkML t = new RunnableSparkML();
		t.runSpark(sml);
	}

	protected static int y = 0;

	@Override
	public double run() {
		RunnableSparkML t = new RunnableSparkML();
		return t.runSpark(this);
	}

}
