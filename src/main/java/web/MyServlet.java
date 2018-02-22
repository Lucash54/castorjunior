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
		
		System.out.println(path);

		BufferedReader br = new BufferedReader(new FileReader(path));	     
	     
		String test = (br.readLine());
		System.out.println(test);
		try {
			lire_csv(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			weka();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.getServletContext().getRequestDispatcher("/myform.html").forward(req, resp);				

    
    }
    
    public void lire_csv(String chemin) throws Exception {
    	
        
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(chemin));
        Instances data = loader.getDataSet();
     
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("src/csv.arff"));
        saver.setDestination(new File("src/csv.arff"));
        saver.writeBatch();
        System.out.println("qsd");
      }   
    
    public static void weka() throws Exception {
        // train classifier
        J48 cls = new J48();
        Instances data = new Instances(new BufferedReader(new FileReader("src/csv.arff")));
        data.setClassIndex(data.numAttributes() - 1);
        cls.buildClassifier(data);

        // display classifier
        final javax.swing.JFrame jf = 
          new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(500,400);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
            cls.graph(),
            new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
          public void windowClosing(java.awt.event.WindowEvent e) {
            jf.dispose();
          }
        });

        jf.setVisible(true);
        tv.fitToScreen();
      }
    
    
    


}
