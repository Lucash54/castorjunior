package web;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import Main.Library;

@SuppressWarnings("serial")
@WebServlet(name = "mytest", urlPatterns = { "/accueil" })
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class MyServlet extends HttpServlet {

	public static String idAlgo = "1";
	private static final String UPLOAD_DIR = "/tmp";
	public String path;
	public String fileName;
	public String vary=null;
	public String lib1=null;
	public String method1=null;
	public String pctapp1= null;
	public String param1= null;
	public int moy1;
	public boolean algo2;
	public String lib2=null;
	public String method2=null;
	public String pctapp2= null;
	public String param2= null;
	public int moy2;
	public boolean algo3;
	public String lib3=null;
	public String method3=null;
	public String pctapp3= null;
	public String param3= null;
	public int moy3;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/* La méthode doPost est appelée lorsqu'on confirme l'envoi du formulaire sur la page web*/
		
		/* On récupère les données*/
		loadFormData(req);

		/*idAlgo permet d'avoir 3 images dans la page*/
		idAlgo="1";
		/* On calcule l'accuracy du premier algorithme*/
		double accuracy1 = calcul(this.pctapp1,this.lib1,this.method1,this.param1,this.moy1);
		/* On ajoute l'accuracy dans les attributs de la page que l'on renvoit*/
		req.setAttribute("res1", accuracy1);
		/*On ajoute l'image du CART pour Weka*/
		if(lib1.equals("Weka") && method1.equals("CART") && this.moy1 == 1) {
			String image = "/tmp/"+fileName.substring(0,fileName.length()-4)+idAlgo+".png";
			req.setAttribute("image1", image);
		}
		/*Pour l'affichage, on garde les paramètres*/
		req.setAttribute("lib1", this.lib1);
		req.setAttribute("method1", this.method1);
		req.setAttribute("pct1", this.pctapp1);
		req.setAttribute("tree1", this.param1);
		req.setAttribute("moy1", this.moy1);

		idAlgo="2";
		/* On fait la même chose avec le deuxième algo (si la case est cochée)*/
		if(!(req.getParameterValues("algo2")==null)) {
			double accuracy2 = calcul(this.pctapp2,this.lib2,this.method2,this.param2,this.moy2);
			req.setAttribute("res2", accuracy2);	
			if(lib2.equals("Weka") && method2.equals("CART") && this.moy2 == 1) {
				String image = "/tmp/"+fileName.substring(0,fileName.length()-4)+idAlgo+".png";
				req.setAttribute("image2", image);
			}
			req.setAttribute("lib2", this.lib2);
			req.setAttribute("method2", this.method2);
			req.setAttribute("pct2", this.pctapp2);
			req.setAttribute("tree2", this.param2);
			req.setAttribute("moy2", this.moy2);
		}

		idAlgo="3";
		/* On fait la même chose avec le deuxième algo (si la case est cochée)*/
		if(!(req.getParameterValues("algo3")==null)) {
			double accuracy3 = calcul(this.pctapp3,this.lib3,this.method3,this.param3,this.moy3);
			req.setAttribute("res3", accuracy3);	
			if(lib3.equals("Weka") && method3.equals("CART") && this.moy3 == 1) {
				String image = "/tmp/"+fileName.substring(0,fileName.length()-4)+idAlgo+".png";
				req.setAttribute("image3", image);
			}
			req.setAttribute("lib3", this.lib3);
			req.setAttribute("method3", this.method3);
			req.setAttribute("pct3", this.pctapp3);
			req.setAttribute("tree3", this.param3);
			req.setAttribute("moy3", this.moy3);
		}
		
		/*Cela nous amène sur la page des réponses*/
		getServletContext().getRequestDispatcher("/res.jsp").forward(req, resp);
	}

	/**
	 * Récupérer le nom du fichier csv
	 */
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}

	/*
	 * Récupère les données inscrites dans le formulaire
	 */
	private void loadFormData(HttpServletRequest req) throws IOException, ServletException {

		/*Chemin de l'appli*/
		String applicationPath = req.getServletContext().getRealPath("");
		/*Chemin du csv*/
		String uploadFilePath = applicationPath +/*"/../.." +*/ File.separator + UPLOAD_DIR;
		/*Crée un dossier pour télécharger les fichiers csv*/
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		String aux = null;
		// Get all the parts from request and write it to the file on server
		for (Part part : req.getParts()) {
			aux = getFileName(part);
			if (!"".equals(aux)) {
				/*Crée le csv dans le dossier*/
				this.fileName=aux;
				part.write(uploadFilePath + File.separator + this.fileName);
				this.path = fileSaveDir.getAbsolutePath()+"/"+this.fileName;
			}else {
				/*Récupère toutes les données du formulaire*/
				StringWriter writer = new StringWriter();
				IOUtils.copy(part.getInputStream(), writer);
				String theString = writer.toString();
				if (part.getHeader("content-disposition").contains("variabley")) {
					this.vary=theString;
				}else if (part.getHeader("content-disposition").contains("lib1")) {
					this.lib1=theString;
				}else if (part.getHeader("content-disposition").contains("lib2")) {
					this.lib2=theString;
				}else if (part.getHeader("content-disposition").contains("lib3")) {
					this.lib3=theString;
				}else if (part.getHeader("content-disposition").contains("method1")) {
					this.method1=theString;
				}else if (part.getHeader("content-disposition").contains("method2")) {
					this.method2=theString;
				}else if (part.getHeader("content-disposition").contains("method3")) {
					this.method3=theString;
				}else if (part.getHeader("content-disposition").contains("pctapp1")) {
					this.pctapp1=theString;
				}else if (part.getHeader("content-disposition").contains("pctapp2")) {
					this.pctapp2=theString;
				}else if (part.getHeader("content-disposition").contains("pctapp3")) {
					this.pctapp3=theString;
				}else if (part.getHeader("content-disposition").contains("param1")) {
					this.param1=theString;
				}else if (part.getHeader("content-disposition").contains("param2")) {
					this.param2=theString;
				}else if (part.getHeader("content-disposition").contains("param3")) {
					this.param3=theString;
				}else if (part.getHeader("content-disposition").contains("moy1")) {
					this.moy1=Integer.parseInt(theString);
				}else if (part.getHeader("content-disposition").contains("moy2")) {
					this.moy2=Integer.parseInt(theString);
				}else if (part.getHeader("content-disposition").contains("moy3")) {
					this.moy3=Integer.parseInt(theString);
				}

			}
		}
	}

	/*
	 * Calcule l'accuracy des algo 
	 */
	private double calcul(String proportion, String librairie, String methode, String arbres,int moyenne) {
		double accuracy = 0;
		for(int i=1;i<=moyenne;i++) {
			/*avoir la proportion entre 0 et 1*/
			double propApp = Double.parseDouble(proportion)/100.0;
			/*avoir un nombre d'arbres = 10 au cas où il y aurait un problème dans la suite*/
			int nbtree=10;
			if(!arbres.equals("")) {
				nbtree=Integer.parseInt(arbres);
			}
			Library l = new Library(this.path, this.vary, propApp, librairie.substring(0, 1).toLowerCase(), methode.substring(0, 1).toLowerCase(), nbtree);
			accuracy = accuracy + l.run();
		}
		accuracy=accuracy/moyenne;
		accuracy=Math.round(accuracy*1000)/1000.0;
		return(accuracy);
	}


}