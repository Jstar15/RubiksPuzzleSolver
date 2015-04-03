package vision;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.RotationForest;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
 
public class WekaMachineLearning {
	private ArrayList<Color> colorarray = new ArrayList<Color>();
	
	public WekaMachineLearning() throws Exception{
		BufferedReader datafiletrain = readDataFile("weka/colortrain.arff");
		Instances train = new Instances(datafiletrain);
		train.setClassIndex(train.numAttributes() - 1);
		
		BufferedReader datafiletest = readDataFile("weka/colortest.arff");
		Instances test = new Instances(datafiletest);
		test.setClassIndex(test.numAttributes() - 1);
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1");  // remove 1st attribute
		// classifier
		RotationForest model =	new RotationForest();

		// meta-classifier
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(rm);
		fc.setClassifier(model);
		// train and make predictions
		fc.buildClassifier(train);
		for (int i = 0; i < test.numInstances(); i++) {
			double pred = fc.classifyInstance(test.instance(i));
		    colorarray.add(getColorFromString(test.classAttribute().value((int) pred)));
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
 

	public ArrayList<Color> getColorarray() {
		return colorarray;
	}

	private Color getColorFromString(String strcolor){
		Color color = null;
		if(strcolor.equals("green")){
			color = Color.GREEN;
		}else if(strcolor.equals("blue")){
			color = Color.BLUE;
		}else if(strcolor.equals("red")){
			color = Color.RED;
		}else if(strcolor.equals("orange")){
			color = Color.ORANGE;
		}else if(strcolor.equals("white")){
			color = Color.WHITE;
		}else if(strcolor.equals("yellow")){
			color = Color.YELLOW;
		}
		
		return color;
	}

}
