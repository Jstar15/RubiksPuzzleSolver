package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class VisionPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public VisionPanel(){
		
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		
		JLabel picLabel = new JLabel(new ImageIcon("IMG.JPG"));
		picLabel.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
		mainpanel.add(picLabel, BorderLayout.NORTH);
		
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
		titlepanel.setBorder(new EmptyBorder(5, 2, 8, 0));
		
		JLabel title = new JLabel("   Visual Recognition");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
		panel.add(titlepanel, BorderLayout.NORTH	);
		panel.add(mainpanel, BorderLayout.CENTER);
		add(panel);
		
	}

}
