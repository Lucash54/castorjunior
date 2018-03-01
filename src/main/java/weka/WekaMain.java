package weka;

public class WekaMain {
	
	public static void main(String[] args) throws Exception {
		try {
			//WekaSVM weksv =  new WekaSVM("./src/iris.csv","Species","0.7");
			//weksv.run();
			WekaCART wekcart =  new WekaCART("./src/iris.csv","Species","0.8");
			wekcart.run();
			//WekaRandomForest wekrf =  new WekaRandomForest("./src/iris.csv","Species","0.6");
			//wekrf.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}