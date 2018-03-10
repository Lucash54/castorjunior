package Main;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Main implements Serializable {

	public static void main(String[] args) {
		
		// On crée une console pour intéragir avec l'utilisateur
		Console console = new Console();
		console.inputMain();
		
		// On crée l libraryavec les données de l'utilisateur
		Library l = new Library(console.strAdresse, console.strVariable, console.propApp, console.lib, console.algo, console.nbTrees);
		
		// On affiche l'accuracy
		System.out.println("");
		System.out.println("Accuracy = "+ l.run());
	}
}
