//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package vision;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IB1;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.RotationForest;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
 

//predicts the color of the cube squares using weka based on a training set and test set (previously generated)
public class WekaMachineLearning {
	private ArrayList<Color> colorarray = new ArrayList<Color>();
	
	public WekaMachineLearning() throws Exception{
		//open arff files as an instance
		BufferedReader datafiletrain = readDataFile("weka/colortrain.arff");
		Instances train = new Instances(datafiletrain);
		train.setClassIndex(train.numAttributes() - 1);
		
		BufferedReader datafiletest = readDataFile("weka/colortest.arff");
		Instances test = new Instances(datafiletest);
		test.setClassIndex(test.numAttributes() - 1);
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1");  // remove 1st attribute
		
		// choose classifier
		IB1 model =	new IB1();
		
		//RotationForest model =	new RotationForest();
		//model.setClassifier(new RandomTree());
		//model.setNumIterations(10);

		

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
	
	//read file and return as buffered reader
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

	//get color from color string
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
