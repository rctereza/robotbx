package com.rctereza.robotbx.ztest;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupLayoutExample2 {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Login Form");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = new JPanel();

			JLabel userLabel = new JLabel("Username:");
			JTextField userField = new JTextField(15);
			
			JLabel passLabel = new JLabel("Password:");
			JPasswordField passField = new JPasswordField(15);
			
			JButton loginButton = new JButton("Login");

			GroupLayout layout = new GroupLayout(panel);
			panel.setLayout(layout);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(userLabel).addComponent(passLabel))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(userField).addComponent(passField).addComponent(loginButton, GroupLayout.Alignment.TRAILING)));

			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(userLabel).addComponent(userField))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(passLabel).addComponent(passField)).addComponent(loginButton));

			frame.setContentPane(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

}
