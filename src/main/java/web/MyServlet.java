package web;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import castorjunior.CSV2Arff;
import castorjunior.WekaTraining;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

@WebServlet(name="mytest",urlPatterns={"/myurl"})
public class MyServlet extends HttpServlet {
	
	public void init() {
		
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            
        PrintWriter p = new PrintWriter(resp.getOutputStream());
        p.print("Hello world ENSAI");
        p.flush();
        
		//this.getServletContext().getRequestDispatcher("/src/main/webapp/myform.html").forward(req, resp);				
    	// CA MARCHE PAS MAIS ON LAISSE AU CAS OU

        
    }
    
    

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
    	//resp.setContentType("/src/main/resources/myform.html");
    	// CA MARCHE PAS MAIS ON LAISSE AU CAS OU
    	
		String path = req.getParameter("chemin");
		CSV2Arff transformateur = new CSV2Arff();
		
		String aux = path.substring(0, path.length()-3)+"arff";
		
		System.out.println(aux);

		/*BufferedReader br = new BufferedReader(new FileReader(path));	     
	     
		String test = (br.readLine());
		System.out.println(test);*/
		
		
		try {
			transformateur.transfo(path,aux);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		WekaTraining wekaTrainer = new WekaTraining();
		
		
		try {
			wekaTrainer.training(aux);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		this.getServletContext().getRequestDispatcher("/myform.html").forward(req, resp);				

    
    }    


}
