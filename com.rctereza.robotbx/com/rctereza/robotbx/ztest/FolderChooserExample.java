package com.rctereza.robotbx.ztest;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FolderChooserExample {

	public static void main(String[] args) {
		// Create the file chooser
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Folder Chooser Example");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 120);
			frame.setLayout(new BorderLayout(10, 10));

			// Text field to display the selected folder
			JTextField folderField = new JTextField();
			folderField.setEditable(false);

			// Button to open the folder chooser
			JButton chooseButton = new JButton("Select Folder");

			chooseButton.addActionListener(e -> {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Select a folder");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				int result = chooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFolder = chooser.getSelectedFile();
					folderField.setText(selectedFolder.getAbsolutePath());
				}
			});

			// Layout: text field at center, button at the right
			frame.add(folderField, BorderLayout.CENTER);
			frame.add(chooseButton, BorderLayout.EAST);

			frame.setLocationRelativeTo(null); // center on screen
			frame.setVisible(true);
		});
	}

}
