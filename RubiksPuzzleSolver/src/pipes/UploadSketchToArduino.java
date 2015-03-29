package pipes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UploadSketchToArduino {
	private String output= null;
	private Boolean success = false;
	public UploadSketchToArduino(String comport){
		
	    //get directory of created sketch file
	    String currentsketchdir = System.getProperty("user.dir") + "\\robot\\robot.ino";
	     
	    //run sketch from command line on specified port
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
        		"cd \"C:\\Program Files (x86)\\Arduino\" && arduino --board arduino:avr:uno --port " + comport +" --upload "+ currentsketchdir);
        builder.redirectErrorStream(true);
        Process p;
        String line;
        String lastline = null;
		try {
			p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        while (true) {
	        	line = r.readLine();
	            if (line == null) { break; }
	            output += line;
	            lastline = line;
	            System.out.println(line);
	       }
		} catch (IOException e) {
			e.printStackTrace();
			}
			
		//check output for an error
		if(output!= null){
			String status = lastline.substring(0, 7);
			if(!status.equals("Problem")){
				success = true;
			}
		}
	}
	
    public Boolean getSuccess() {
		return success;
	}

	public String getOutput() {
		return output;
	}

	public static void main(String[] args) throws Exception {
    	UploadSketchToArduino x = new UploadSketchToArduino("COM3");
    }
}
