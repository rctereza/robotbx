package com.rctereza.robotbx.ztest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class MigLayoutCheatSheet {
	public static void main(String[] args) {
		JFrame frame = new JFrame("MigLayout Cheat Sheet");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);

		JPanel panel = new JPanel(new MigLayout(
				"", // Column constraints
				"[][grow][grow]", // 3 columns: first default, next two grow
				"[][][][][][]" // 6 rows
		));

		// Row 1: Labels and text fields
		panel.add(new JLabel("Name:"), "align right");
		panel.add(new JTextField(10), "wrap");
		panel.add(new JLabel("Email:"), "align right");
		panel.add(new JTextField(10), "wrap");

		// Row 2: TextArea spanning multiple columns
		panel.add(new JLabel("Comments:"), "align right");
		panel.add(new JTextArea(3, 20), "span 2, grow, wrap");

		// Row 3: Buttons of same size
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		JButton apply = new JButton("Apply");
		panel.add(ok, "sizegroup btn, split 3");
		panel.add(cancel, "sizegroup btn");
		panel.add(apply, "sizegroup btn, wrap");

		// Row 4: Alignment demo
		panel.add(new JButton("Left"), "align left");
		panel.add(new JButton("Center"), "align center");
		panel.add(new JButton("Right"), "align right, wrap");

		// Row 5: Gaps
		panel.add(new JButton("Gap Left 20"), "gapleft 20");
		panel.add(new JButton("Gap Right 20"), "gapright 20");
		panel.add(new JButton("Gap Top 10, Bottom 10"), "gaptop 10, gapbottom 10, wrap");

		// Row 6: Split cell demo
		panel.add(new JButton("Yes"), "split 2, sizegroup confirm");
		panel.add(new JButton("No"), "sizegroup confirm, wrap");

		frame.add(panel);
		frame.setVisible(true);
	}
}
