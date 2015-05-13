# RubiksPuzzleSolver
Solve a rubiks cube using vision and robotics. Final year project.

## System Requirements: 

Operating System:Microsoft Windows 7 (x64)

## Software Requirements:

OpenCV 3.0 beta (environmental variables must be set)
Weka 3.6.12
Java VM 1.7
Arduino 1.6.4 IDE software
 
## Hardware Requirements: 

A Webcam: software tested using a Logitech QuickCam Pro 3000 
A Rubik's cube solving robot (For robotic capability's)

## Prerequisites
User library's must be added to the the Java project for both openCV and weka so they can be added to the build path on runtime. The JRE system library must also be added to the build path.

## Usage
First start by opening the project in eclipse and then run Gui.java located in the src/gui/ package

###Normal Mode
First align cube in front of webcam and select analyze.  A preview will be displayed, you may need to analyze multiple times to get the desired output. 
Then select the face you have captured and select update. The preview will then be copied to the cube. (Note the preview will be flipped because it’s a mirror image).
The reset button will simply change the preview back to its default display.

ALternatively you can input the cube state manually. This can be done by simply selecting a cube color and then selecting the corresponding square on the cube representation diagram. 
Do this for every square so that all squares on all faces are identical to the Rubik’s cube you are trying to solve.


###Training Mode
Training mode allows you to add datasets to the Weka training file. This will help improve results in normal mode.
To get into Training mode select the training tab from the mode menu in the top left hand corner of the application.
Select the color you want to train, align cube showing a cubes face with only the specified color in the camera preview and select train.
NOTE: It is important that you only train the desired color with a cube face that is only that color. This ensures that false data is not added to the training set. This would ultimately damage the prediction rate.


## Credits

The cube solving algorithm implemented in this application was developed by Mike Reid. The program is written in C and was modified to work with this project.Found here:
https://github.com/jdkoftinoff/mb-linux-msli/blob/master/uClinux-dist/user/games/rubik/miker.c

