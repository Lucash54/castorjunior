package castorjunior;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class cocoo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 DataSource source = new DataSource("data/iris2.arff");
		 Instances data = source.getDataSet();

	}

}
