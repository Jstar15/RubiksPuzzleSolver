package robotics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CreateArduinoSketch {
	private StringBuilder s = new StringBuilder();							
	private CubeObject cube = new CubeObject();
	
	public CreateArduinoSketch(String moves){
		//moves = "Solution: F B2 U2 L' U2 F' R B2 R' D (14q, 10f)";  //example input
		String[] movesarray = GetMovesFromStr(moves);
		CreateAndSaveSketch(movesarray);
	}
	
	private void MoveCube90Clockwise(){
		s.append("MoveCube90Clockwise();");
		cube.MoveCube90Clockwise();;
	}
	
	private void MoveCube90AntiClockwise(){
		s.append("MoveCube90AntiClockwise();");
		cube.MoveCube90AntiClockwise();
	}
	private void MoveCube180(){
		s.append("MoveCube180();");
		cube.MoveCube180();
	}
	
	private void RotateCube90Clockwise(){
		s.append("RotateCube90Clockwise();");
	}
	
	private void RotateCube90AntiClockwise(){
		s.append("RotateCube90AntiClockwise();");
	}
	
	private void RotateCube180(){
		s.append("RotateCube180();");
	}
	
	private void FlipCube(){
		s.append("FlickOverCube();");
		cube.FlipCube();
	
	}
	
	
	private String getBuilderStr(){
		return s.toString();
	}
	
	
	private String[] GetMovesFromStr(String moves){
		int start = moves.indexOf(": ")+1;
		int end = moves.indexOf(" (");
		String movearray[] = moves.substring(start, end).trim().split(" ");
		return movearray;
	}
	
	private String[] getSketchTemplate(){
		String content = readFile("robot/SketchTemplate.txt");
		return content.split("split here");
	}
	
	private String readFile(String filename){
	   String content = null;
	   FileReader reader = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	   } catch (IOException e) {
	       e.printStackTrace();
	   }finally {
		   try {reader.close();} catch (Exception ex) {}
		}
	   return content;
	}

	private void WriteToFile(String filename, String content){
		BufferedWriter output = null;
        try {
            File file = new File(filename);
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
          } catch ( IOException e ) {
             e.printStackTrace();
          }finally {
   		   try {output.close();} catch (Exception ex) {}
  		}
	}
	
	private void CreateAndSaveSketch(String[] moves){
		String[] sketchtemplate = getSketchTemplate();
		
		s.append(sketchtemplate[0]);
		for(int index = 0; index < moves.length; index++){
			DetermineRobotMove(moves[index]);
		}
		s.append(sketchtemplate[1]);
		WriteToFile("robot/robot.ino",  getBuilderStr());
	}
	
	private void DetermineRobotMove(String move){
		if(move.length()>0){
			if(move.charAt(0) == 'F'){
				FromFaceToFace('F');
				TurnCubeFace('F', move);
			}else if(move.charAt(0) == 'U'){
				FromFaceToFace('U');
				TurnCubeFace('U', move);
			}else if(move.charAt(0) == 'R'){
				FromFaceToFace('R');
				TurnCubeFace('R', move);
			}else if(move.charAt(0) == 'D'){
				FromFaceToFace('D');
				TurnCubeFace('D', move);
			}else if(move.charAt(0) == 'L'){
				FromFaceToFace('L');
				TurnCubeFace('L', move);
			}else if(move.charAt(0) == 'B'){
				FromFaceToFace('B');
				TurnCubeFace('B', move);
			}
		}
	}
	
	
	
	private void FromFaceToFace(char targetface){
		if(targetface == cube.getDown()){
			// do nothing 
		}else if(targetface == cube.getFront()){
			FlipCube();
		}else if(targetface == cube.getUp()){
			FlipCube();
			FlipCube();
		}else if(targetface == cube.getLeft()){
			MoveCube90Clockwise();
			FlipCube();
		}else if(targetface == cube.getRight()){
			MoveCube90AntiClockwise();
			FlipCube();
		}else if (targetface == cube.getBack()){
			MoveCube180();
			FlipCube();
		}
	}
	
	private void TurnCubeFace(char targetface, String move){	
		if(move.length() == 1){
			RotateCube90Clockwise();
		}else{
			if(move.charAt(1)  == '2'){
				RotateCube180();
			}else if(move.charAt(1)  == '\''){
				RotateCube90AntiClockwise();
			}else{
				System.out.println("error unknown move");
			}
		}
	}
}
