package com.rctereza.robotbx.ztest;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class ShowSVG {

	public static void main(String[] args) {
		// Setup FlatLaf
		FlatLightLaf.setup();

		JFrame frame = new JFrame("FlatLaf SVG Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);

		// Load SVG as an icon
//		Icon lightIcon = new ImageIcon(ShowSVG.class.getResource("/images/light.svg"));
//		System.out.println(lightIcon.toString()); 
		
		FlatSVGIcon lightIcon = new FlatSVGIcon("/images/light.svg", 64, 64);
		
		System.out.println(lightIcon.getImage()); 

		// Use it in a label or button
		JLabel label = new JLabel("Here is an SVG:", lightIcon, SwingConstants.CENTER);
		frame.add(label, BorderLayout.CENTER);

		frame.setVisible(true);
	}

}
