package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pipes.UploadSketchToArduino;

public class ArduinoPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String[] comports = { "COM1", "COM2", "COM3", "COM4", "COM5" };
	private JComboBox combolist = new JComboBox(comports);
	
	public ArduinoPanel(){
		setLayout(new BorderLayout());
		setVisible(false);
		setBounds(460, 120 ,260, 30);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		JButton robotb = new JButton("Initiate Robot");
		robotb.setFocusable(false);
		robotb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Thread t1 = new Thread(new Runnable() {
					public void run(){
				    	String portselection =  (String) combolist.getItemAt(combolist.getSelectedIndex());
				    	UploadSketchToArduino x = new UploadSketchToArduino(portselection);
				    	if(x.getSuccess() == false){
				    		JOptionPane.showMessageDialog(null,  x.getOutput(), "Status Message", JOptionPane.INFORMATION_MESSAGE);
				    	}else{
				    		JOptionPane.showMessageDialog(null,  "Uploaded to arduino sucsessfully", "Status Message", JOptionPane.INFORMATION_MESSAGE);
				    	}
				        }});  
				        t1.start();
				    }
				});				
		panel.add(robotb);
		
		combolist.setSelectedIndex(2);
		combolist.setFocusable(false);
	
		add(combolist,  BorderLayout.CENTER);
		add(panel,  BorderLayout.EAST);
	}
}
