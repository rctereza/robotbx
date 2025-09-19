package com.rctereza.robotbx.ztest;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

public class MigLayoutPlayground {

	public static void main(String[] args) {
        JFrame frame = new JFrame("MigLayout Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        JPanel mainPanel = new JPanel(new MigLayout(
                "", "[grow][grow][grow][grow]", "[][][][][][][][][][]"
        ));

        // Helper to style components based on constraint type
        java.util.function.BiConsumer<JComponent, String> styleComponent = (comp, info) -> {
            comp.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            comp.setToolTipText(info);

            if (info.contains("align")) comp.setBackground(new Color(173, 216, 230)); // Blue
            else if (info.contains("span") || info.contains("grow")) comp.setBackground(new Color(144, 238, 144)); // Green
            else if (info.contains("sizegroup") || info.contains("split")) comp.setBackground(new Color(255, 182, 193)); // Pink
            else if (info.contains("gap")) comp.setBackground(new Color(255, 250, 205)); // Yellow
            else if (info.contains("push")) comp.setBackground(new Color(221, 160, 221)); // Purple
            else comp.setBackground(new Color(245, 245, 245)); // Gray default

            comp.setOpaque(true);
        };

        // Row 1: Basic labels and text fields
        JLabel lblName = new JLabel("Name:");
        styleComponent.accept(lblName, "align right");
        mainPanel.add(lblName, "align right");

        JTextField tfName = new JTextField(10);
        styleComponent.accept(tfName, "growx");
        mainPanel.add(tfName, "growx, wrap");

        JLabel lblEmail = new JLabel("Email:");
        styleComponent.accept(lblEmail, "align right");
        mainPanel.add(lblEmail, "align right");

        JTextField tfEmail = new JTextField(10);
        styleComponent.accept(tfEmail, "growx");
        mainPanel.add(tfEmail, "growx, wrap");

        // Row 2: Nested panel with its own MigLayout
        JPanel nestedPanel = new JPanel(new MigLayout("", "[grow][grow]", "[]"));
        nestedPanel.setBorder(new LineBorder(Color.BLACK, 1));
        styleComponent.accept(nestedPanel, "Nested Panel");

        JButton nestedBtn1 = new JButton("Nested 1");
        JButton nestedBtn2 = new JButton("Nested 2");
        styleComponent.accept(nestedBtn1, "grow");
        styleComponent.accept(nestedBtn2, "grow");

        nestedPanel.add(nestedBtn1, "grow");
        nestedPanel.add(nestedBtn2, "grow");
        mainPanel.add(nestedPanel, "span 4, grow, wrap");

        // Row 3: TextArea with span, grow, push
        JTextArea taComments = new JTextArea(3, 25);
        styleComponent.accept(taComments, "span 4, grow, push");
        mainPanel.add(taComments, "span 4, grow, push, wrap");

        // Row 4: Buttons with sizegroup and split
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        JButton btnApply = new JButton("Apply");
        styleComponent.accept(btnOK, "sizegroup buttons, split 3, growy");
        styleComponent.accept(btnCancel, "sizegroup buttons, growy");
        styleComponent.accept(btnApply, "sizegroup buttons, growy");

        mainPanel.add(btnOK, "sizegroup buttons, split 3, growy");
        mainPanel.add(btnCancel, "sizegroup buttons, growy");
        mainPanel.add(btnApply, "sizegroup buttons, growy, wrap");

        // Row 5: Alignment demo
        JButton leftBtn = new JButton("Left");
        JButton centerBtn = new JButton("Center");
        JButton rightBtn = new JButton("Right");
        styleComponent.accept(leftBtn, "align left");
        styleComponent.accept(centerBtn, "align center");
        styleComponent.accept(rightBtn, "align right");

        mainPanel.add(leftBtn, "align left");
        mainPanel.add(centerBtn, "align center");
        mainPanel.add(rightBtn, "align right, wrap");

        // Row 6: Push demo
        JButton btnPush = new JButton("Push & Grow");
        styleComponent.accept(btnPush, "grow, push");
        mainPanel.add(btnPush, "span 4, grow, push, wrap");

        // Row 7: Min/Max size example
        JTextField tfMin = new JTextField(10);
        tfMin.setMinimumSize(new Dimension(80, 25));
        styleComponent.accept(tfMin, "min size 80x25");

        JTextField tfMax = new JTextField(10);
        tfMax.setMaximumSize(new Dimension(150, 25));
        styleComponent.accept(tfMax, "max size 150x25");

        mainPanel.add(tfMin);
        mainPanel.add(tfMax, "wrap");

        // Row 8: Gap demo
        JButton btnGap1 = new JButton("Gap Left 20");
        JButton btnGap2 = new JButton("Gap Right 20");
        styleComponent.accept(btnGap1, "gapleft 20");
        styleComponent.accept(btnGap2, "gapright 20");

        mainPanel.add(btnGap1, "gapleft 20");
        mainPanel.add(btnGap2, "gapright 20, wrap");

        // Row 9: Split cell
        JButton btnYes = new JButton("Yes");
        JButton btnNo = new JButton("No");
        styleComponent.accept(btnYes, "split 2, sizegroup confirm");
        styleComponent.accept(btnNo, "sizegroup confirm, wrap");

        mainPanel.add(btnYes, "split 2, sizegroup confirm");
        mainPanel.add(btnNo, "sizegroup confirm, wrap");

        // Row 10: Nested spans with alignment
        JButton nestedSpanLeft = new JButton("Nested Left");
        JButton nestedSpanCenter = new JButton("Nested Center");
        JButton nestedSpanRight = new JButton("Nested Right");
        styleComponent.accept(nestedSpanLeft, "span 1, align left");
        styleComponent.accept(nestedSpanCenter, "span 2, align center");
        styleComponent.accept(nestedSpanRight, "span 1, align right");

        mainPanel.add(nestedSpanLeft, "span 1, align left");
        mainPanel.add(nestedSpanCenter, "span 2, align center");
        mainPanel.add(nestedSpanRight, "span 1, align right, wrap");

        mainPanel.setBackground(new Color(240, 240, 240));
        frame.add(new JScrollPane(mainPanel)); // Scrollable playground
        frame.setVisible(true);
    }
}
