package weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

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
	* prend un .csv en entrée, un chemin vers un fichier .arff, et un numero de variable à tranformer en var nominale
	* et le convertit en .arff (structure de donnéee utilisée par weka)
	*/
	
	public void transfo(String csv, String arff, String varY) throws Exception{
 
	    // charge le csv
	    CSVLoader loader = new CSVLoader();
	    loader.setSource(new File(csv));
	    Instances data = loader.getDataSet();
	    
	    // On crée un filter nous permettant de rendre la variable à expliquer nominale si elle est numeric. 
	    NumericToNominal filter1 = new NumericToNominal();
	    filter1.setInputFormat(data);
	    
	    // On récupère l'indice de la variable à expliquer
	    int n = data.attribute(varY).index()+1;
	    filter1.setAttributeIndices(""+n);
	   
	    // On utilise le filter
	    data = Filter.useFilter(data, filter1);
	    
	    // On sauve l'arff
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

	public void transfo(String csv,String varY) throws Exception {
	  transfo(csv,"src/default.arff",varY);
	}
  
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  