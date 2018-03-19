package rengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;

import web.MyServlet;

public class RengineSVM extends RengineMethod {

	public RengineSVM(String strAdresse, String strVariable, double propApp, ScriptEngine engine) {
		super(strAdresse, strVariable, propApp, engine);
	}

	public double run() {
		double accuracy = 0;

	    try{
	    	
	    	// on crée  un lecteur de fichier r pour lire le script du support vector machine ligne à ligne 
	    	
	    	InputStream flux = new FileInputStream("src/svm.R");
	    	InputStreamReader lecture = new InputStreamReader(flux);
	    	BufferedReader buff = new BufferedReader(lecture);
	    	String ligne;
	    	
	    	// tant que le fichier n'est pas entièrement lu, càd qu'on arrive pas à la dernière ligne
	    	String output="";
	    	StringWriter outputWriter=null;
	    	while ((ligne=buff.readLine())!=null){
	    		outputWriter = new StringWriter();
		    	engine.getContext().setWriter(outputWriter);
	    		System.out.println(engine.eval(ligne));
	    		output = outputWriter.toString();
	    		// on évalue le code R associé à la ligne lue
	    	}
	    	
	    	// Permet de changer la virgule en point pour transformer le string en double
	    	String output2 ="";
	    	if(output.contains(",")) {
	    		output2 = output.split(",")[0]+"."+output.split(",")[1];
	    	}else {
	    		output2 = output;
	    	}
	    	accuracy = Double.parseDouble(output2.split(" ")[1]);
	    	accuracy = Math.round(accuracy*1000.0)/1000.0;
	    	
	    	// on cherche par suite à sauvegarder sous java la matrice de confusion associée au modèle
	    	Integer nbOcc = Integer.parseInt(engine.eval("length(lab)").toString().substring(2,3));
	    	// on définit le nombre d'occurences
	    	
	    	// définition de la matrice
	    	String[][] conf = new String[nbOcc+1][nbOcc+1];
	    	
	    	// la première case, c'est réel vs predict
	    	conf[0][0] = "Real\\Pred";
	    	
	    	// les labels des réels et des predictions
	    	for(int i=1; i <=nbOcc; i++) {
	    		conf[i][0] = engine.eval("print(lab["+i+"])").toString();
	    		conf[0][i] = engine.eval("print(lab["+i+"])").toString();
	    	}
	    	
	    	// les valeurs des effectifs par val pred et réelles
	    	for(int i=1; i <= nbOcc;i++) {
	    		for(int j=1; j <= nbOcc; j++) {
	    			conf[i][j] = engine.eval("print(as.numeric(conf["+i+","+j+"]))").toString()
	    					.substring(0,engine.eval("print(as.numeric(conf["+i+","+j+"]))").toString().length()-2);
	    		}
	    	}
	    	
	    	// puis on crée une image de la matrice de confusion
	    	
    	    BufferedImage img = new BufferedImage((nbOcc+2)*100, (nbOcc+2)*100, BufferedImage.TYPE_INT_RGB);
    	    // nbOcc plus deux car on veut la marge à gauche et celle à droite
    	    
    	    // puis on crée le pinceau
	    	Graphics g = img.getGraphics();
	    	
	    	// on colore d'abord le fond en blanc
	    	g.setColor(Color.white);
	    	g.fillRect(0, 0, img.getHeight(), img.getWidth());
	    	
	    	// puis on passe en noir pour l'écriture des valeurs
	    	g.setColor(Color.black);
	    	
	    	g.drawLine(70, 70, 70,100*(nbOcc+1)+70);
	    	g.drawLine(70, 70,100*(nbOcc+1)+70,70);
	    	
	    	for(int i=0; i <= nbOcc;i++) {
	    		for(int j=0; j <= nbOcc;j++) {
	    			g.drawLine(100*(i+1)+70,70, 100*(i+1)+70,100*(j+1)+70);
	    			g.drawLine(70,100*(j+1)+70,100*(i+1)+70,100*(j+1)+70);
	    			g.drawString(conf[i][j],100*(i+1), 100*(j+1));
	    		}
	    	}
	    	
	    	// enfin, on écrit dans un fichier qui sera stocké, puis mis sur la page web
	    	String nomTableau = strAdresse.substring(0, strAdresse.length()-4);
	    	File outputfile = new File(nomTableau+MyServlet.idAlgo+".png");
    	    ImageIO.write(img, "png", outputfile);
	    	
	    	// une fois le code exécuté, on ferme le buffer. L'accuracy est la dernière ligne affichée.
	    	buff.close();
    	}		
	    
    	catch (Exception e){
    		System.out.println(e.toString());
    	}
		return accuracy;
	    
	  }

}
