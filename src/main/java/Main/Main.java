package Main;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Main implements Serializable {

	public static void main(String[] args) {
		
		Console console = new Console();
		console.inputMain();
		
		Library l = new Library(console.strAdresse, console.strVariable, console.propApp, console.lib, console.algo, console.nbTrees);
		System.out.println("");
		System.out.println("Accuracy = "+ l.run());

		
		//TODO : essayer de faire que si on se trompe, on peut réécrire
	}
	
}
