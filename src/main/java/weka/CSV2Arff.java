package weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
 
public class CSV2Arff {
	
	/** 
	* On utilise un design pattern singleton 
	* pour créer une seule instance de classe pour tout le projet
	* donc constructeur privé et appel à l'instance public
	*/
	
	private static volatile CSV2Arff instance = null;
	
	private CSV2Arff() {}
	
	public final static CSV2Arff getInstance() {
		
        if (CSV2Arff.instance == null) {
           // On évite la multi-instanciation avec le synchronized
           synchronized(CSV2Arff.class) {
             if (CSV2Arff.instance == null) {
            	 CSV2Arff.instance = new CSV2Arff();
             }
           }
        }
        return CSV2Arff.instance;
    }

	
	/**
	* prend un .csv en entrée, 
	* et le convertit en .arff (structure de donnéee utilisée par weka)
	*/
	
	public void transfo(String csv, String arff) throws Exception{
 
	    // charge le csv
	    CSVLoader loader = new CSVLoader();
	    loader.setSource(new File(csv));
	    Instances data = loader.getDataSet();
	 
	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(arff));
	    saver.setDestination(new File(arff));
	    saver.writeBatch();
	 }

	
	/**
	* Alternative à la fonction précédente, 
	* avec un répertoire par défault pour le fichier .arff
	*/

	public void transfo(String csv) throws Exception {
	  transfo(csv,"src/default.arff");
	}
  
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  