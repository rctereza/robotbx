package com.rctereza.robotbx.tools;

import javax.swing.*;
import java.awt.*;

public class AutoCloseMessageDialog {

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread
//        SwingUtilities.invokeLater(() -> {
//            showAutoCloseMessage("This message will close in 3 seconds", "Auto-Close", 3000);
//        });
    	show("This message will close in 3 seconds", "Auto-Close", 3000);
        System.out.println("DOne!");
        
    }

    /**
     * Shows a message dialog that closes automatically after a given delay.
     *
     * @param message The message to display
     * @param title   The dialog title
     * @param delayMs Delay in milliseconds before closing
     */
    public static void show(String message, String title, int delayMs) {
        // Create a JOptionPane
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{}, // No buttons
                null
        );

        // Create a dialog from the option pane
        JDialog dialog = optionPane.createDialog((Frame) null, title);

        // Create a Swing Timer to close the dialog after delayMs
        Timer timer = new Timer(delayMs, e -> dialog.dispose());
        timer.setRepeats(false); // Only run once
        timer.start();

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
