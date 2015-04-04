//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import pipes.RunCommand;
import robotics.CreateArduinoSketch;
import vision.CaptureImage;

public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton[][] facelet = new JButton[6][9];
	private JButton[] pfacelet = new JButton[9];
	private JButton[] selectionfacelet = new JButton[6];
	private final JButton[] colorSel = new JButton[6];
	private JEditorPane textArea = new JEditorPane();
	private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.orange, Color.blue };
	private Color curCol = COLORS[0]; 
	private JPanel arduinopanel = new ArduinoPanel();
	private CaptureImage win = new CaptureImage();
	private ArrayList<Color> colorarray;
	private JButton updatebutton;
	private int face;
	private JMenuBar menubar;
	private JButton previewtrainb = new JButton();
	private JPanel bottomvisionpanel;
	private JPanel visionpanel;
	
	//initiate JFrame
	public static void main(String[] args) {
		Gui frame = new Gui();
		frame.setVisible(true);
	}

	//setup basic layout and preferences for jframe
	public Gui() {
		
		//setup  dimensions and settings
		super("kociemba's sub-optimal cube algorithm");
		createmenubar();
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		setContentPane(container);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1200, 800);
	    setLocationRelativeTo(null);
	    setMinimumSize(new Dimension(1300, 960));

	    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CubePanel(), OutputPanel());
        splitPane.setResizeWeight(0.83);
        splitPane.setOpaque(false);
        
        if(win.getWebcamavailable()){
             visionpanel = VisionPanel();
        }else{
        	 visionpanel = new JPanel();
        	 JOptionPane.showMessageDialog(null, "Webcam not detected, vision functionality has been disabled", "Warning",JOptionPane.WARNING_MESSAGE);
        }

		container.add(TitlePanel(), BorderLayout.NORTH	);
		container.add(splitPane, BorderLayout.CENTER);
		container.add(visionpanel,  BorderLayout.EAST	);
	}
	
	//menu bar //allows toggle between training and normal mode
	public void createmenubar(){
		//create menu bar
		menubar = new JMenuBar();
        add(menubar);
        
        JMenu mode = new JMenu("Mode");
        menubar.add(mode);

        JMenuItem normalmode = new JMenuItem("Normal Mode");
        normalmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ResetPreviewPanel();
				ChangeVisionPanelMode(false);
			}
		});
        
        JMenuItem trainingmode = new JMenuItem("Training Mode");
        trainingmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ResetPreviewPanel();
				curCol = COLORS[0];
				ChangeVisionPanelMode(true);
			}
		});
        
        mode.add(normalmode);
        mode.add(trainingmode);
      
        setJMenuBar(menubar);
	}
	
	//main title panel
	private JPanel TitlePanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
	    panel.setBackground(Color.GRAY);
		panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 55 ));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel leftpanel = new JPanel();
		leftpanel.setLayout(new BorderLayout());
		leftpanel.setBackground(Color.GRAY);
		
		JLabel title = new JLabel("   Rubiks Puzzle Solver");
		title.setFont(new Font("Serif", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		
		JLabel name = new JLabel("    By Jordan Waddell");
		name.setFont(new Font("Serif", Font.BOLD, 14));
		name.setForeground(Color.WHITE);
		name.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		leftpanel.add(title,  BorderLayout.WEST);
		leftpanel.add(name,  BorderLayout.EAST);
		
		panel.add(leftpanel,  BorderLayout.WEST);
		
		return panel;
	} 
		
	
	//output panel that will display output to the user 
	private JPanel OutputPanel(){
		JPanel outputpanel = new JPanel();
		outputpanel.setLayout(new GridLayout(1, 1));
		outputpanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
		
		textArea.setFont(new Font("Serif", Font.PLAIN, 16));
		textArea.setEditable(false);
		textArea.setText("");
	
		outputpanel.add(textArea);

		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
		titlepanel.setBorder(new EmptyBorder(5, 2, 8, 0));
		
		JLabel title = new JLabel("   Program Output");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(titlepanel,  BorderLayout.NORTH);
		panel.add(outputpanel,  BorderLayout.CENTER);
	
		return panel;
	} 
		
	
	//this panel allows the user to input the location of a color on the cube manually
	private JPanel CubePanel() {
		int FSIZE = 60;
		int[] XOFF = { 4, 7, 4, 4, 1, 10 };
		int[] YOFF = { 1, 4, 4, 7, 4, 4 };
		
		JPanel panel = new JPanel();
		
		//generate cube squares then style and position them
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++) {
				facelet[i][j] = new JButton();
				getContentPane().add(facelet[i][j]);
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
				facelet[i][j].setOpaque(true);
				facelet[i][j].setBounds(FSIZE * XOFF[i] + FSIZE * (j % 3), FSIZE * YOFF[i] + FSIZE * (j / 3), FSIZE, FSIZE);
				facelet[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						((JButton) evt.getSource()).setBackground(curCol);
					}
				});
			}
		
		//set texts on the center of each cube representing a unique face
		String[] txt = { "U", "R", "F", "D", "L", "B" };
		for (int i = 0; i < 6; i++){
			facelet[i][4].setText(txt[i]);
		}
		
		
		//generate color options and listener
		for (int i = 0; i < 6; i++) {
			colorSel[i] = new JButton();
			getContentPane().add(colorSel[i]);
			colorSel[i].setBackground(COLORS[i]);
			colorSel[i].setOpaque(true);
			colorSel[i].setBounds(FSIZE * (XOFF[1] + 1) + FSIZE / 4 * 3 * i, FSIZE * (YOFF[3] + 1), FSIZE / 4 * 3, FSIZE / 4 * 3);
			colorSel[i].setName("" + i);
			colorSel[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					curCol = COLORS[Integer.parseInt(((JButton) evt.getSource()).getName())];  //get color selection
					previewtrainb.setBackground(curCol);
				}
			});
		}

		JButton resetb = new JButton("Reset Cube");
		getContentPane().add(resetb);
		resetb.setBounds(460, 180 ,120, 30);
		resetb.setFocusable(false);
		resetb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
				ResetCubeState();
		    }
		});
		
		JButton solveb = new JButton("Solve Cube");
		getContentPane().add(solveb);
		solveb.setBounds(600, 180 ,120, 30);
		solveb.setFocusable(false);
		solveb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	textArea.setText(" Solving cube ... This may take a few moments");
		        Thread t1 = new Thread(new Runnable() {
		            public void run(){
				    	solveCube();
		        }});  
		        t1.start();
		    }
		});
		
		getContentPane().add(arduinopanel);
		
		return panel;
	}

	//take the users input and parse it into a string where it can be passed to the cube solving algorithm 
	private void solveCube() {
		StringBuffer s = new StringBuffer(54);

		for (int i = 0; i < 54; i++){
			s.insert(i, 'B');// default initialization
		}
		
		// read the 54 facelets and append to stringbuffer
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 9; j++) {
				if (facelet[i][j].getBackground() == facelet[0][4].getBackground())
					s.setCharAt(9 * i + j, 'U');
				if (facelet[i][j].getBackground() == facelet[1][4].getBackground())
					s.setCharAt(9 * i + j, 'R');
				if (facelet[i][j].getBackground() == facelet[2][4].getBackground())
					s.setCharAt(9 * i + j, 'F');
				if (facelet[i][j].getBackground() == facelet[3][4].getBackground())
					s.setCharAt(9 * i + j, 'D');
				if (facelet[i][j].getBackground() == facelet[4][4].getBackground())
					s.setCharAt(9 * i + j, 'L');
				if (facelet[i][j].getBackground() == facelet[5][4].getBackground())
					s.setCharAt(9 * i + j, 'B');
			}
		}
		
		//organise cube string into input that the cube solving program can read
		String formatedstate = FormatStringForCubeState(s.toString());
		CallCubeSolvingProgram(formatedstate); 
	}
	
	
	//This panel contains all vision features such webcam output, preview and controls
	private JPanel VisionPanel(){
		
		JPanel visionpanel = new JPanel();
		visionpanel.setLayout(new BorderLayout());
		
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
		titlepanel.setBorder(new EmptyBorder(5, 0, 8, 0));
		
		JLabel title = new JLabel("   Visual Recognition                                      ");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
        win.start();
        win.setBorder(new EmptyBorder(290, 0, 0, 0));

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(titlepanel, BorderLayout.NORTH);
		panel.add(win, BorderLayout.CENTER);

		JPanel previewpanel = new JPanel();
		previewpanel.setLayout(new BorderLayout());
		previewpanel.add(PreviewPanel(), BorderLayout.NORTH);
		
		JPanel toppanels = new JPanel();
		toppanels.setLayout(new BorderLayout());
		toppanels.add(panel, BorderLayout.NORTH);
		toppanels.add(previewpanel, BorderLayout.CENTER);
			
		bottomvisionpanel = new JPanel();
		bottomvisionpanel.setLayout(new BorderLayout());
		bottomvisionpanel.add(CubeOptionForVision(), BorderLayout.CENTER);
		bottomvisionpanel.add(VisionControlPanel(), BorderLayout.SOUTH);
		
		visionpanel.add(toppanels, BorderLayout.NORTH);
		visionpanel.add(bottomvisionpanel, BorderLayout.CENTER);
		
		return visionpanel;
	}
	
	//switch between training mode and normal mode
	public void ChangeVisionPanelMode(Boolean trainmode){
		bottomvisionpanel.removeAll();
		if(trainmode){
			bottomvisionpanel.add(CubeOptionTrainingVision(), BorderLayout.CENTER);
			bottomvisionpanel.add(VisionTrainingPanel(), BorderLayout.SOUTH);
		}else{
			bottomvisionpanel.add(CubeOptionForVision(), BorderLayout.CENTER);
			bottomvisionpanel.add(VisionControlPanel(), BorderLayout.SOUTH);
		}
		
		bottomvisionpanel.revalidate();
		bottomvisionpanel.repaint();

		
	}
	
	//options and buttons to control vision panel
	private JPanel VisionControlPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JButton analysebutton = new JButton(" Analyse ");
		analysebutton.setFocusable(false);
		analysebutton .addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				 ResetVisionOptionPanel();
				 updatebutton.setEnabled(false);
				 CapturePixels(false);
				 EnableorDisableFaceSelection(true);
			}
		});
		
		JPanel updatepanel = new JPanel();
		updatepanel.setLayout(new BorderLayout());
		
		updatebutton = new JButton("Update ");
		updatebutton.setEnabled(false);
		updatebutton.setFocusable(false);
		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				colorarray = FlipColorArray(colorarray);
				InputCubeFaceFromArray(face,  colorarray);
				updatebutton.setEnabled(false);
				ResetVisionOptionPanel();
				EnableorDisableFaceSelection(false);
			}
		});
		updatepanel.add(new JLabel("    "), BorderLayout.WEST);
		updatepanel.add(updatebutton, BorderLayout.CENTER);
		updatepanel.add(new JLabel("    "), BorderLayout.EAST);
		
		
		JButton resetbutton = new JButton("  Reset   ");
		resetbutton.setFocusable(false);
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (int x = 0; x < 9; x++) {
					updatebutton.setEnabled(false);
					ResetVisionOptionPanel();
					EnableorDisableFaceSelection(false);
					ResetPreviewPanel();
				}
			}
		});
	
		panel.add( analysebutton, BorderLayout.WEST);
		panel.add( updatepanel, BorderLayout.CENTER);
		panel.add( resetbutton, BorderLayout.EAST);
		
		return panel;
		
	}
	
	//buttons for vision panel when training
	private JPanel VisionTrainingPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JButton analysebutton = new JButton(" Train ");
		analysebutton.setFocusable(false);
		analysebutton .addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				 CapturePixels(true);
			}
		});
		
		panel.add( new JLabel("  "), BorderLayout.WEST);
		panel.add( analysebutton, BorderLayout.CENTER);
		panel.add( new JLabel("  "), BorderLayout.EAST);
		
		return panel;
	}
	
	//panel showing preview of color selection for training
	private JPanel CubeOptionTrainingVision(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(20, 20, 0, 20));
		
		JLabel label = new JLabel("Select a color for training    >>>");
		JPanel previewpanel = new JPanel();
		previewpanel.setLayout(new BorderLayout());
		
		previewtrainb = new JButton();
		previewtrainb.setPreferredSize(new Dimension(60, 50));
		previewtrainb.setEnabled(false);
		previewtrainb.setOpaque(true);
		previewtrainb.setBackground(Color.white);
		previewtrainb.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		previewpanel.add( label, BorderLayout.WEST);
		previewpanel.add( previewtrainb, BorderLayout.EAST);
		
		panel.add( previewpanel, BorderLayout.NORTH);

		return panel;
		
	}
	
	//panel that provides user with a face selection for when inputting state via vision
	private JPanel CubeOptionForVision(){
		
		String[] txt = { "U", "L", "F", "R", "B", "D" };
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel optionpanel = new JPanel();
		optionpanel.setLayout(new GridLayout(3, 4));
		
		optionpanel.setBorder(BorderFactory.createMatteBorder(2, 4, 2, 5, Color.BLACK));
		int n = 0;
		for (int i = 0; i < 12; i++) {
			if(i == 0 || i == 2 || i == 3 || i == 8 || i ==10 || i ==11){
				optionpanel.add(new JLabel());
			}else{
				selectionfacelet[n] = new JButton(txt[n]);
				selectionfacelet[n].setBackground(Color.gray);
				selectionfacelet[n].setRolloverEnabled(false);
				selectionfacelet[n].setFocusable(false);
				selectionfacelet[n].setEnabled(false);
				selectionfacelet[n].setPreferredSize(new Dimension(50, 60));
				selectionfacelet[n].setOpaque(true);
				selectionfacelet[n].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						ResetVisionOptionPanel();
						((JButton) evt.getSource()).setBackground(Color.WHITE);
						String visionfaceselection = ((JButton) evt.getSource()).getText();
						face =  FaceSelectionToInt(visionfaceselection);
						updatebutton.setEnabled(true);
					}
				});
				optionpanel.add(selectionfacelet[n]);
				n++;
			}
		}
		panel.add(optionpanel, BorderLayout.NORTH);
		
		return panel;
	} 
	
	//panel showing preview of predicted square colors for vision
	private JPanel PreviewPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel previewpanel = new JPanel();
		previewpanel.setLayout(new GridLayout(3, 3, 5, 5));
		previewpanel.setBorder(BorderFactory.createMatteBorder(2, 4, 4, 4, Color.BLACK));
		
		for (int i = 0; i < 9; i++) {
			pfacelet[i] = new JButton("  ");
			pfacelet[i].setBackground(Color.gray);
			pfacelet[i].setRolloverEnabled(false);
			pfacelet[i].setPreferredSize(new Dimension(75, 80));
			pfacelet[i].setEnabled( false ); 
			pfacelet[i].setOpaque(true);
			previewpanel.add(pfacelet[i]);
		}
		   
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
        titlepanel.setBorder(new EmptyBorder(5, 0, 8, 0));
		
		JLabel title = new JLabel("   Preview");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
		panel.add( titlepanel, BorderLayout.NORTH);
		panel.add( previewpanel, BorderLayout.CENTER);
		
		
		return panel;
	}
	
	//call cube solving program using RunCommand class
	private void CallCubeSolvingProgram (String state){
		RunCommand c = new RunCommand("miker/miker.exe", state );
		textArea.setText(c.getSolution());
		if(c.getSuccess()){
			new CreateArduinoSketch(c.getSolution());
			arduinopanel.setVisible(true);
		}else{
			arduinopanel.setVisible(false);
		}
	}
	
	
	//update a face based on predicted square colors for vision
	private void InputCubeFaceFromArray(int face, ArrayList<Color> colorarray){
		for (int x = 0; x < colorarray.size(); x++) {
			facelet[face][x].setBackground(colorarray.get(x));
		}
	}
	
	//reset cube state input to default
	private void ResetCubeState() {
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 9; j++) {
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
			}
		}
		textArea.setText("");
		arduinopanel.setVisible(false);
	}
	
	//reset vision option panel to default
	private void ResetVisionOptionPanel(){
		for (int i = 0; i < 6; i++) {
			selectionfacelet[i].setBackground(Color.gray);
			selectionfacelet[i].setRolloverEnabled(false);
			selectionfacelet[i].setOpaque(true);
		}
	}
	
	//reset preview for vision to default
	private void ResetPreviewPanel(){
		for (int i = 0; i < 9; i++) {
			pfacelet[i].setBackground(Color.gray);
			pfacelet[i].setRolloverEnabled(false);
			pfacelet[i].setOpaque(true);
		}
	}
	
	//enable or disable button clicks for face selection in vision panel normal mode
	private void EnableorDisableFaceSelection(Boolean boo){
		for (int i = 0; i < 6; i++) {
			selectionfacelet[i].setEnabled(boo);
		}
	}
	
	//flip array because image was originally flipped for preview purposes.
	private ArrayList<Color> FlipColorArray(ArrayList<Color> arraylist){
		ArrayList<Color> flippedarraylist = new ArrayList<Color>( arraylist);
		flippedarraylist.set(0, arraylist.get(2));
		flippedarraylist.set(3, arraylist.get(5));
		flippedarraylist.set(6, arraylist.get(8));
		
		flippedarraylist.set(2, arraylist.get(0));
		flippedarraylist.set(5, arraylist.get(3));
		flippedarraylist.set(8, arraylist.get(6));
		return flippedarraylist;
	}
	
	//sorts string array into a format that can be passed to the solver program
	private String FormatStringForCubeState(String cubestr){
		int[][] indexorder ={{7, 19},{5, 10},{1, 46},{3, 37},{28, 25},{32, 16},{34, 52},{30, 43},{23, 12},{21, 41},{48, 14},{50, 39},	
				{8, 20, 9},{2, 11, 45},{0, 47, 36},{6, 38, 18},{29, 15, 26},{27, 24, 44}, {33, 42, 53},{35, 51, 17}};
		
		StringBuilder sb = new StringBuilder(100);
		
		for(int x=0; x < indexorder.length; x++){
			for(int i=0; i < indexorder[x].length; i++){
				sb.append(cubestr.charAt(indexorder[x][i]));
			}
			sb.append(",");
		}
		return sb.toString();
		
	}
	
	//get image and show in preview for vision panel
	private void CapturePixels(Boolean training){
		win.CapturePixels(training, curCol);
		
		//break loop when colorarray when preview is ready
		while(true){
	    	try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	if(win.getPreviewready()){
	    		win.setPreviewready(false);
	    		colorarray = win.GetColorArray();
	    		break;
	    	}
		}

		//set background to preview panel for vision color detection
		for (int i = 0; i < colorarray.size(); i++) {
			pfacelet[i].setBackground(colorarray.get(i));
		}
	
	}
	
	//convert face side string to an int // used with Color[] COLORS global variable
	private int FaceSelectionToInt(String face){
		if(face.equals("U")){
			return 0;
		}else if(face.equals("R")){
			return 1;
		}else if(face.equals("F")){
			return 2;
		}else if(face.equals("D")){
			return 3;
		}else if(face.equals("L")){
			return 4;
		}else if(face.equals("B")){
			return 5;
		}else{
			return -1;
		}
	}
}
