package com.rctereza.robotbx.ztest;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormExample {

	public static void main(String[] args) {
		
        JFrame frame = new JFrame("Form Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Name:", "Email:", "Password:"};
        int y = 0;

        for (String label : labels) {
            gbc.gridx = 0; gbc.gridy = y;
            panel.add(new JLabel(label), gbc);

            gbc.gridx = 1;
            panel.add(new JTextField(15), gbc);
            y++;
        }

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panel.add(new JButton("Submit"), gbc);

        frame.add(panel);
        frame.setVisible(true);
        
    }

}
