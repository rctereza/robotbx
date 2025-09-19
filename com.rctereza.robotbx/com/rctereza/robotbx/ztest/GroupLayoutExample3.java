package com.rctereza.robotbx.ztest;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupLayoutExample3 {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Advanced GroupLayout Form");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = new JPanel();

			// Components
			JLabel nameLabel = new JLabel("Name:");
			JTextField nameField = new JTextField(20);

			JLabel emailLabel = new JLabel("Email:");
			JTextField emailField = new JTextField(20);

			JLabel passwordLabel = new JLabel("Password:");
			JPasswordField passwordField = new JPasswordField(20);

			JButton okButton = new JButton("OK");
			JButton cancelButton = new JButton("Cancel");

			// Layout setup
			GroupLayout layout = new GroupLayout(panel);
			panel.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			// Horizontal group
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(nameLabel).addComponent(emailLabel).addComponent(passwordLabel))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameField).addComponent(emailField).addComponent(passwordField)))
					.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(okButton).addComponent(cancelButton)));

			// Vertical group
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel)
							.addComponent(nameField))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(emailLabel)
							.addComponent(emailField))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(passwordLabel)
							.addComponent(passwordField))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton)
							.addComponent(cancelButton)));

			frame.setContentPane(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

}
