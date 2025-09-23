package com.rctereza.robotbx.ztest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class MigLayoutSameCell {
	
	static JPanel panelMain;
	static JPanel panelComponents; 
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Label + Field in Same Cell");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);

		panelComponents = getNamePanel1();

		JButton button = new JButton("Hide it!");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelMain.remove(panelComponents);
				panelComponents = getNamePanel2();
				panelMain.add(panelComponents);
				panelMain.revalidate();
				panelMain.repaint();
			}
		});

		// 3 columns, 1 row
		panelMain = new JPanel(new MigLayout("", "[grow][grow][grow]", "[][]"));
		panelMain.add(button, "wrap");
		panelMain.add(panelComponents, "split, span, grow");

		frame.add(panelMain);
		frame.setVisible(true);
	}
	
	private static JPanel getNamePanel1() {
		JPanel panel = new JPanel(new MigLayout("", "[][]", "[]"));

		JLabel lblName = new JLabel("Name:");
		JTextField tfName = new JTextField(15);

		panel.add(lblName, "alignx left");
		panel.add(tfName, "growx"); // grows horizontally
		
		return panel;
	}
	
	private static JPanel getNamePanel2() {
		JPanel panel = new JPanel(new MigLayout("", "[][]", "[]"));

		JLabel lblName = new JLabel("Name2:");
		JTextField tfName = new JTextField(15);

		panel.add(lblName, "alignx left");
		panel.add(tfName, "growx"); // grows horizontally
		
		return panel;
	}

}
