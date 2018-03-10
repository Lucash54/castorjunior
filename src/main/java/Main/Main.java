package Main;

import java.io.Serializable;

/**
 * En lançant cette main, nous pouvons renseigner les données nécessaires au calcul et va donner l'accuracy.
 * Sinon, dans chacun des autres packages, il y a une main pour tester directement la librairie concernée par le package
 * 
 * 
 */
@SuppressWarnings("serial")
public class Main implements Serializable {

	public static void main(String[] args) {
		
		// On crée une console pour intéragir avec l'utilisateur
		Console console = new Console();
		console.inputMain();
		
		// On crée la library avec les données de l'utilisateur
		Library l = new Library(console.strAdresse, console.strVariable, console.propApp, console.lib, console.algo, console.nbTrees);
		
		// On affiche l'accuracy
		System.out.println("");
		System.out.println("Accuracy = "+ l.run());
	}
}
