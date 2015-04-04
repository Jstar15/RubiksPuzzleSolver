//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package vision;

import java.awt.*;

//object used to hold RGB value of a pixel
public class SquareColor {
    private double red;
    private double blue;
    private double green;

    public SquareColor(double red, double blue, double green){
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public SquareColor(Color color) {
        red = color.getRed();
        blue = color.getBlue();
        green = color.getGreen();
	}

	public void set(Color color){
        this.red = color.getRed();
        this.blue = color.getBlue();
        this.green = color.getGreen();
    }

    public double getRed(){
        return this.red;
    }

    public double getBlue(){
        return this.blue;
    }

    public double getGreen(){
        return this.green;
    }


}
