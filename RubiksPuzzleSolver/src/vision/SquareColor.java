//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package vision;

import java.awt.*;

//object used to hold RGB value of a pixel
public class SquareColor {
    private int red;
    private int blue;
    private int green;

    public SquareColor(int red, int blue, int green){
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

    public int getRed(){
        return this.red;
    }

    public int getBlue(){
        return this.blue;
    }

    public int getGreen(){
        return this.green;
    }


}
