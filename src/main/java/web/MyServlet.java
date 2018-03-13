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

@WebServlet(name = "mytest", urlPatterns = { "/accueil" })
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class MyServlet extends HttpServlet {

	private static final String UPLOAD_DIR = "/tmp";
	public String vary=null;
	public String lib1=null;
	public String method1=null;
	public String pctapp1= null;
	public String param1= null;
	public String lib2=null;
	public String method2=null;
	public String pctapp2= null;
	public String param2= null;
	public String lib3=null;
	public String method3=null;
	public String pctapp3= null;
	public String param3= null;


	public void init() {

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/*
		 * PrintWriter p = new PrintWriter(resp.getOutputStream());
		 * p.print("Hello world ENSAI"); p.flush();
		 */

		// CA MARCHE PAS MAIS ON LAISSE AU CAS OU

		System.out.println("getget");
		this.getServletContext().getRequestDispatcher("/index.html").forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// resp.setContentType("/src/main/resources/myform.html");
		// CA MARCHE PAS MAIS ON LAISSE AU CAS OU
		/*
		 * System.out.println("debut post");
		 * 
		 * String path = req.getParameter("chemin");
		 * 
		 * Enumeration paramaterNames = req.getParameterNames();
		 * while(paramaterNames.hasMoreElements() ) {
		 * System.out.println(paramaterNames.nextElement()); }
		 * 
		 * System.out.println(path);
		 * 
		 * 
		 * 
		 * CSV2Arff transformateur = new CSV2Arff();
		 * 
		 * String aux = path.substring(0, path.length()-3)+"arff";
		 * 
		 * System.out.println(aux);
		 * 
		 * /*BufferedReader br = new BufferedReader(new FileReader(path));
		 * 
		 * String test = (br.readLine()); System.out.println(test);
		 * 
		 * 
		 * try { transformateur.transfo(path,aux); } catch (Exception e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * 
		 * WekaTraining wekaTrainer = new WekaTraining();
		 * 
		 * 
		 * try { wekaTrainer.training(aux); } catch (Exception e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * System.out.println("finpost");
		 * 
		 * 
		 * this.getServletContext().getRequestDispatcher("/myform.html").forward(req,
		 * resp);
		 * 
		 */
		
				loadFormData(req);
				System.out.println(this.vary);
				getServletContext().getRequestDispatcher("/res.html").forward(req, resp);
	}

	/**
	 * Utility method to get file name from HTTP header content-disposition
	 */
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		//System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			//System.err.println(token);
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}
	
	
	private void loadFormData(HttpServletRequest req) throws IOException, ServletException {

		// gets absolute path of the web application
		String applicationPath = req.getServletContext().getRealPath("");
		// constructs path of the directory reqto save uploaded file
		String uploadFilePath = applicationPath +"/../.." + File.separator + UPLOAD_DIR;

		// creates the save directory if it does not exists
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		System.out.println("Upload File Directory=" + fileSaveDir.getAbsolutePath());

		String fileName = null;
		// Get all the parts from request and write it to the file on server
		for (Part part : req.getParts()) {
			fileName = getFileName(part);
			if (!"".equals(fileName))
				part.write(uploadFilePath + File.separator + fileName);
			else {
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
				}
				
			}
		}
		
		//req.setAttribute("message", fileName + " File uploaded successfully!");

	}
}