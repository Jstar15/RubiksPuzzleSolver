package vision;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
 
public class WekaMachineLearning {
	private ArrayList<String> colorarray = new ArrayList<String>();
	
	public WekaMachineLearning() throws Exception{
		BufferedReader datafiletrain = readDataFile("colortrain.arff");
		Instances train = new Instances(datafiletrain);
		train.setClassIndex(train.numAttributes() - 1);
		
		BufferedReader datafiletest = readDataFile("colortest.arff");
		Instances test = new Instances(datafiletest);
		test.setClassIndex(test.numAttributes() - 1);
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1");  // remove 1st attribute
		// classifier
		MultilayerPerceptron model =	new MultilayerPerceptron();

		// meta-classifier
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(rm);
		fc.setClassifier(model);
		// train and make predictions
		fc.buildClassifier(train);
		for (int i = 0; i < test.numInstances(); i++) {
			double pred = fc.classifyInstance(test.instance(i));
		    System.out.print("ID: " + test.instance(i).value(0));
		    System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
		    System.out.println(", predicted: " + test.classAttribute().value((int) pred));
		    colorarray.add(test.classAttribute().value((int) pred));
		    
		 }
	}	
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.out.println("Error file not found: " + filename);
		}
 
		return inputReader;
	}
 

	public ArrayList<String> getColorarray() {
		return colorarray;
	}

	public static void main(String[] args) throws Exception {
		
	
	}

}
