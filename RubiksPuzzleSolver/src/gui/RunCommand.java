package gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class RunCommand {
	private String solution = null;
	private Boolean success = false;
	public RunCommand(String command, String cubestate){
		try {
			//cubestate = "UF,UR,UB,UL,LB,DB,DL,RD,FR,FL,DF,BR,LDF,RDB,DLB,RUF,LUB,DRF,FUL,BUR,";
			Process p = Runtime.getRuntime().exec(command +" "+ cubestate);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				 solution = line;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(solution!= null){
			String status = solution.substring(0, 5);
			if(!status.equals("Error")){
				solution = "Solution: "+ solution;
				success = true;
			}
		}

	}
	public Boolean getSuccess() {
		return success;
	}
	public String getSolution() {
		return solution;
	}
	
	
}
