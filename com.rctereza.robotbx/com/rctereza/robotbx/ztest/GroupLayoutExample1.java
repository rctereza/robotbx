package com.rctereza.robotbx.ztest;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupLayoutExample1 {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			
			JFrame frame = new JFrame("GroupLayout Example");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel panel = new JPanel();

			JLabel nameLabel = new JLabel("Name:");
			JTextField nameField = new JTextField(20);
			JButton submitButton = new JButton("Submit");

			GroupLayout layout = new GroupLayout(panel);
			panel.setLayout(layout);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			// Horizontal layout
			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameLabel))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameField)
							.addComponent(submitButton)));

			// Vertical layout
			layout.setVerticalGroup(
					layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(nameLabel).addComponent(nameField)).addComponent(submitButton));

			frame.setContentPane(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

}
