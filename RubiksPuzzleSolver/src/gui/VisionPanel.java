package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vision.CaptureImage;

public class VisionPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private CaptureImage win = new CaptureImage();
	public VisionPanel(){
		
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
		titlepanel.setBorder(new EmptyBorder(5, 2, 8, 0));
		
		JLabel title = new JLabel("   Visual Recognition                                      ");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
        win.start();
		
		panel.add(titlepanel, BorderLayout.NORTH);
		panel.add(win, BorderLayout.CENTER);

		add(panel);
		
	}
	
	public void CapturePixels(){
		win.CapturePixels();
	}
}
