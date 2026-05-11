package com.rctereza.robotbx.ztest;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class MoveExternalWindowExample extends JFrame {

	private static final long serialVersionUID = -6395215966099374706L;

	public MoveExternalWindowExample() {

        setTitle("My App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("Open Centered Notepad");

        button.addActionListener(e -> {

            try {

                // Detect current monitor
                GraphicsDevice monitor =
                        getCurrentMonitor();

                Rectangle bounds =
                        monitor.getDefaultConfiguration()
                               .getBounds();

                // Desired window size
                int windowWidth = 800;
                int windowHeight = 600;

                // Center calculation
                int x = bounds.x
                        + (bounds.width - windowWidth) / 2;

                int y = bounds.y
                        + (bounds.height - windowHeight) / 2;

                // Open notepad
                ProcessBuilder pb =
                        new ProcessBuilder("notepad.exe");

                pb.start();

                // Wait for window
                HWND hwnd = null;

                for (int i = 0; i < 20; i++) {

                    hwnd = User32.INSTANCE.FindWindow(
                            null,
                            "Untitled - Notepad");

                    if (hwnd != null) {
                        break;
                    }

                    Thread.sleep(200);
                }

                // Move window
                if (hwnd != null) {

                    User32.INSTANCE.MoveWindow(
                            hwnd,
                            x,
                            y,
                            windowWidth,
                            windowHeight,
                            true);

                    System.out.println(
                            "Notepad centered!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(button);
    }

    private GraphicsDevice getCurrentMonitor() {

        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();

        Rectangle frameBounds = getBounds();

        for (GraphicsDevice device : ge.getScreenDevices()) {

            Rectangle monitorBounds =
                    device.getDefaultConfiguration().getBounds();

            if (monitorBounds.intersects(frameBounds)) {
                return device;
            }
        }

        return ge.getDefaultScreenDevice();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MoveExternalWindowExample().setVisible(true);
        });
    }
}