package com;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI  implements ActionListener{
	
	private Main parent;
	private JFrame frame;
	private JPanel panel;
	private JButton install, fault, onoff;
	
	private int toggle = 0;
	
	public GUI(Main parent){
		this.parent = parent;
		
		setUpGUI();
		
	}
	
	private void setUpGUI(){
		setUpButtons();
		setUpPanel();
		setUpFrame();
	}
	
	private void setUpFrame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void setUpPanel(){
		panel = new JPanel(new BorderLayout());
		panel.add(install, BorderLayout.PAGE_START);
		panel.add(onoff, BorderLayout.CENTER);
		panel.add(fault, BorderLayout.PAGE_END);
		panel.setVisible(true);
	}
	
	private void setUpButtons(){
		install = new JButton("Install");
		install.setVerticalAlignment(AbstractButton.CENTER);
		install.setHorizontalTextPosition(AbstractButton.CENTER);
		install.setActionCommand("install");
		install.setVisible(true);
		install.addActionListener(this);
		fault = new JButton("Fault");
		fault.setVerticalAlignment(AbstractButton.CENTER);
		fault.setHorizontalTextPosition(AbstractButton.CENTER);
		fault.setActionCommand("fault");
		fault.addActionListener(this);
		fault.setVisible(true);
		onoff = new JButton("Off");
		onoff.setVerticalAlignment(AbstractButton.CENTER);
		onoff.setHorizontalTextPosition(AbstractButton.CENTER);
		onoff.setActionCommand("toggle");
		onoff.addActionListener(this);
		onoff.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		if(action.equals("fault")){
			parent.setFault();
		}else if(action.equals("install")){
			parent.setInstall();
		}else if(action.equals("toggle")){
			if(onoff.getText().equals("Off")){
				onoff.setText("On");
			}else{
				onoff.setText("Off");
			}
			parent.toggle();
		}
	}
	
}
