package weka;

public class WekaMain {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		try {
			WekaSVM weksv =  new WekaSVM("./src/iris.csv","Species","0.7");
			weksv.training();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
