//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package vision;

import java.util.ArrayList;

//determines the mean, std dev and varience for an arrayList of SquareColor(rgb values), //each rgb value represents a pixel in an image
public class Statistics {

	private SquareColor mean;
	private SquareColor stddev;
	private SquareColor varience;
    
    private  ArrayList<SquareColor> data;
    
    private double size;
    
    public Statistics(ArrayList<SquareColor>  data) {
    	this.data = data;
        size = data.size();
        DetermineMean();
        DetermineVariance();
        DetermineStdDev();
    }

    public SquareColor getMean() {
		return mean;
	}

	public SquareColor getStddev() {
		return stddev;
	}

	public SquareColor getVarience() {
		return varience;
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

    private void DetermineStdDev() {
    	
    	double red = Math.sqrt(varience.getRed());
    	double blue = Math.sqrt(varience.getBlue());
    	double green =	Math.sqrt(varience.getGreen());
    	
    	stddev = new SquareColor(red, blue, green);
    	
    }
}
