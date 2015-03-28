package gui;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;

public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton[][] facelet = new JButton[6][9];
	private final JButton[] colorSel = new JButton[6];
	private JEditorPane textArea = new JEditorPane();
	private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.orange, Color.blue };
	private Color curCol = COLORS[0]; 

	public static void main(String[] args) {
		Gui frame = new Gui();
		frame.setVisible(true);
	}

	public Gui() {
		super("kociemba's sub-optimal cube algorithm");
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		setContentPane(container);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1200, 800);
	    setLocationRelativeTo(null);
	    setMinimumSize(new Dimension(1200, 800));

	    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CubePanel(), OutputPanel());
        splitPane.setResizeWeight(0.75);
        splitPane.setOpaque(false);
		container.add(splitPane, BorderLayout.CENTER);

	}
	
	private JPanel OutputPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		textArea.setFont(new Font("Serif", Font.PLAIN, 14));
		textArea.setEditable(false);
		textArea.setText("hello");
		textArea.setContentType("text/html; charset=UTF-8");
	
		panel.add(textArea);
	
		return panel;
	} 
		
	private JPanel CubePanel() {
		int FSIZE = 60;
		int[] XOFF = { 4, 7, 4, 4, 1, 10 };
		int[] YOFF = { 0, 3, 3, 6, 3, 3 };
		
		JPanel panel = new JPanel();
		
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
		
		String[] txt = { "U", "R", "F", "D", "L", "B" };
		
		for (int i = 0; i < 6; i++)
			facelet[i][4].setText(txt[i]);
		
		for (int i = 0; i < 6; i++) {
			colorSel[i] = new JButton();
			getContentPane().add(colorSel[i]);
			colorSel[i].setBackground(COLORS[i]);
			colorSel[i].setOpaque(true);
			colorSel[i].setBounds(FSIZE * (XOFF[1] + 1) + FSIZE / 4 * 3 * i, FSIZE * (YOFF[3] + 1), FSIZE / 4 * 3, FSIZE / 4 * 3);
			colorSel[i].setName("" + i);
			colorSel[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					curCol = COLORS[Integer.parseInt(((JButton) evt.getSource()).getName())];
				}
			});
		}

		JButton resetb = new JButton("Reset Cube");
		getContentPane().add(resetb);
		resetb.setBounds(460, 125 ,120, 30);
		resetb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {

		    }
		});
		
		JButton solveb = new JButton("Solve Cube");
		getContentPane().add(solveb);
		solveb.setBounds(600, 125 ,120, 30);
		solveb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	textArea.setText("Solving cube ... \nThis may take a few moments");
		        Thread t1 = new Thread(new Runnable() {
		            public void run(){
				    	
		            }});  
		            t1.start();
		    }
		});
		
		return panel;
	}

	
}
