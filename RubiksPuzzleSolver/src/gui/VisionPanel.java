package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent ;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vision.CaptureImage;

public class VisionPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private CaptureImage win = new CaptureImage();
	private JButton[][] facelet = new JButton[3][3];
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
		prepanel.add(PreviewPanel(), BorderLayout.CENTER);
		
		
		add(prepanel, BorderLayout.CENTER);
		add(VisionControlPanel(), BorderLayout.SOUTH);


		
	}
	
	public JPanel VisionControlPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton analysebutton = new JButton(" Analyse ");
		JButton resetbutton = new JButton("  Reset   ");
		
		panel.add( analysebutton, BorderLayout.WEST);
		panel.add( resetbutton, BorderLayout.EAST);
		
		return panel;
		
	}
	
	public JPanel PreviewPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel previewpanel = new JPanel();
		previewpanel.setLayout(new GridLayout(3,3));
		previewpanel.setBorder(BorderFactory.createMatteBorder(2, 4, 2, 5, Color.BLACK));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++){
				facelet[i][j] = new JButton("  ");
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
				facelet[i][j].setEnabled( false ); 
				facelet[i][j].setOpaque(true);
				facelet[i][j].addActionListener(new ActionListener(){
			    @Override
			    public void actionPerformed(ActionEvent evt){
			    	((JButton) evt.getSource()).setBackground(curCol);
			    	
			    	}
			    });
			        previewpanel.add(facelet[i][j]);
			}
			
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
	
	public void CapturePixels(){
		win.CapturePixels();
	}
}
