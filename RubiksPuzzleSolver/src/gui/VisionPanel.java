package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent ;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vision.CaptureImage;

public class VisionPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private CaptureImage win = new CaptureImage();
	private JButton[] facelet = new JButton[9];
	private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.orange, Color.blue };
	private Color curCol = COLORS[0]; 
	public VisionPanel(){
		
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

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

		panel.add(titlepanel, BorderLayout.NORTH);
		panel.add(win, BorderLayout.CENTER);
		
		add(panel, BorderLayout.NORTH);
		
		
		JPanel prepanel = new JPanel();
		prepanel.setLayout(new BorderLayout());
		prepanel.add(PreviewPanel(), BorderLayout.NORTH);
		
		
		add(prepanel, BorderLayout.CENTER);
		add(VisionControlPanel(), BorderLayout.SOUTH);


		
	}
	
	public JPanel VisionControlPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton analysebutton = new JButton(" Analyse ");
		analysebutton .addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				 CapturePixels();
			}
		});
		
		JButton resetbutton = new JButton("  Reset   ");
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				((JButton) evt.getSource()).setBackground(curCol);
			}
		});
		
		
		
		panel.add( analysebutton, BorderLayout.WEST);
		panel.add( resetbutton, BorderLayout.EAST);
		
		return panel;
		
	}
	
	public JPanel PreviewPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(0, 0, 300, 0));
		JPanel previewpanel = new JPanel();
		previewpanel.setLayout(new GridLayout(3, 3, 5, 5));
		
		previewpanel.setBorder(BorderFactory.createMatteBorder(2, 4, 2, 5, Color.BLACK));
		for (int i = 0; i < 9; i++) {
			
				facelet[i] = new JButton("  ");
				facelet[i].setBackground(Color.gray);
				facelet[i].setRolloverEnabled(false);
				facelet[i].setPreferredSize(new Dimension(75, 90));
				facelet[i].setEnabled( false ); 
				facelet[i].setOpaque(true);
				facelet[i].addActionListener(new ActionListener(){
			    @Override
			    public void actionPerformed(ActionEvent evt){
			    	((JButton) evt.getSource()).setBackground(curCol);
			    	
			    	}
			    });
			        previewpanel.add(facelet[i]);
		
			
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
	
	public void ShowPreview(){
		
	}
	
	public void CapturePixels(){
		win.CapturePixels();
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
		ArrayList<String> colorarray = win.GetColorArray();
		for (int i = 0; i < colorarray.size(); i++) {
			Color color = getColorFromString(colorarray.get(i));
			facelet[i].setBackground(color);
		}
	}
	
	private Color getColorFromString(String strcolor){
		Color color = null;
		if(strcolor.equals("green")){
			color = Color.GREEN;
		}else if(strcolor.equals("blue")){
			color = Color.BLUE;
		}else if(strcolor.equals("red")){
			color = Color.RED;
		}else if(strcolor.equals("orange")){
			color = Color.ORANGE;
		}else if(strcolor.equals("white")){
			color = Color.WHITE;
		}else if(strcolor.equals("yellow")){
			color = Color.YELLOW;
		}
		
		return color;
	}
}
