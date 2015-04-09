//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package vision;

import java.util.ArrayList;
import java.util.Collections;

//determines the mean, std dev and varience for an arrayList of SquareColor(rgb values), //each rgb value represents a pixel in an image
public class Statistics {

	private SquareColor mean;
	private SquareColor varience;
	private SquareColor median;
    
    private  ArrayList<SquareColor> data;
    
    private double size;
    
    public Statistics(ArrayList<SquareColor>  data) {
    	this.data = data;
        size = data.size();
        
        DetermineMean();
        DetermineVariance();
        DetermineMedian();
    }

    public SquareColor getMean() {
		return mean;
	}

	public SquareColor getVarience() {
		return varience;
	}

	
	public SquareColor getMedian() {
		return median;
	}

	private void DetermineMean() {
    	double red = 0;
    	double blue = 0;
    	double green =0;
    	
    	for(SquareColor c : data){
    		red += c.getRed();
    		blue += c.getBlue();
    		green += c.getGreen();
    	}
 
    	red = red / size;
    	blue = blue / size;
    	green = green / size;
    	
    	mean = new SquareColor(red, blue, green);
    }

    private void DetermineVariance() {
    	double redmean = mean.getRed();
    	double bluemean = mean.getBlue();
    	double greenmean =mean.getGreen();
    	
    	double red = 0;
    	double blue = 0;
    	double green =0;
    	
    	for(SquareColor c : data){
            red += (redmean - c.getRed()) * (redmean - c.getRed());
            blue += (bluemean - c.getBlue()) * (bluemean - c.getBlue());
            green += (greenmean - c.getGreen()) * (greenmean - c.getGreen());
        }
    	
    	red = red / size;
    	blue = blue / size;
    	green = green / size;
    	
    	varience = new SquareColor(red, blue, green);
    }
    
    private void DetermineMedian(){
    	ArrayList<Double> redarray = new ArrayList<Double>();
    	ArrayList<Double> bluearray= new ArrayList<Double>();
    	ArrayList<Double> greenarray= new ArrayList<Double>();
    	
    	for(SquareColor c : data){
    		redarray.add(c.getRed());
    		bluearray.add(c.getBlue());
    		greenarray.add(c.getGreen());
    	}
    	Collections.sort(redarray);
    	Collections.sort(bluearray);
    	Collections.sort(greenarray);
    	
    	double red = getMedianFromArray(redarray);;
    	double blue = getMedianFromArray(bluearray);;
    	double green =getMedianFromArray(greenarray);;

    	median = new SquareColor(red, blue, green);
    }
    
    private Double getMedianFromArray(ArrayList<Double> array){
    	if(size == 0){
    		return (double) 0;
    	}if(size % 2 == 1){ //odd number 
    		return array.get((int) (size/2));
    	}
    	return (array.get((int) ((size / 2)-1)) + array.get((int) (size / 2))) / 2;
    }

}
