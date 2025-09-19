package com.rctereza.robotbx.ztest;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GroupLayoutExample {

	/*
	| Alignment  | Effect (LTR locale)         | Common Use Case                         |
	| ---------- | --------------------------- | --------------------------------------- |
	| `LEADING`  | Aligns to **left/start**    | Left-aligned labels, fields             |
	| `TRAILING` | Aligns to **right/end**     | Right-aligned labels, OK/Cancel buttons |
	| `CENTER`   | Aligns to **center**        | Centering a button group                |
	| `BASELINE` | Aligns to text **baseline** | Labels aligned with text fields         |
	*/
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("GroupLayout Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		// Auto gaps
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel nameLabel = new JLabel("Name:");
		JTextField nameField = new JTextField(15);

		JLabel emailLabel = new JLabel("Email:");
		JTextField emailField = new JTextField(15);

		JButton submitBtn = new JButton("Submit");

		// Horizontal group
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(nameLabel).addComponent(emailLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameField).addComponent(emailField).addComponent(submitBtn)));

		// Vertical group
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(emailLabel).addComponent(emailField))
				.addComponent(submitBtn));

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
