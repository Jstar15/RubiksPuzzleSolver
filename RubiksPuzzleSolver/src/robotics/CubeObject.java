package robotics;

public class CubeObject {
	private char front = 'F';
	private char up = 'U';
	private char right = 'R';
	private char down = 'D'; 
	private char left ='L';
	private char back ='B';
	
	
	public void FlipCube(){
		char frontt = up;
		char upt = back;
		char downt = front; 
		char backt = down;
		
		front = frontt;
		up = upt;
		down = downt;
		back = backt;
	}

	public void MoveCube90AntiClockwise(){
		char leftt = front;
		char frontt = right;
		char rightt = back; 
		char backt = left;
		
		left = leftt;
		front = frontt;
		right = rightt;
		back = backt;
	}
	
	public void MoveCube90Clockwise(){
		char leftt = back;
		char frontt = left;
		char rightt = front; 
		char backt = right;
		
		left = leftt;
		front = frontt;
		right = rightt;
		back = backt;
	}

	public void MoveCube180(){
		char leftt = right;
		char frontt = back;
		char rightt = left; 
		char backt = front;
		
		left = leftt;
		front = frontt;
		right = rightt;
		back = backt;
	}
	
	public char getFront() {
		return front;
	}

	public char getUp() {
		return up;
	}
	
	public char getRight() {
		return right;
	}

	public char getDown() {
		return down;
	}

	public char getLeft() {
		return left;
	}

	public char getBack() {
		return back;
	}
	
	public void printState(){
		System.out.println("front "+ front);
		System.out.println("up "+ up);
		System.out.println("right "+ right);
		System.out.println("left "+ left);
		System.out.println("down "+ down);
		System.out.println("back "+ back);
		
		
	}
	
}
