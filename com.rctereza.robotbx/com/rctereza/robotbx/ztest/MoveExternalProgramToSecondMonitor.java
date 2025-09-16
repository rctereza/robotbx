package com.rctereza.robotbx.ztest;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import com.rctereza.robotbx.Constants;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class MoveExternalProgramToSecondMonitor {

	public static void main(String[] args) throws Exception {
		// 1. Detect monitors
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

		if (screens.length < 2) {
			System.out.println("Only one monitor detected. Exiting...");
			return;
		}

		// Choose the second monitor (index 1)
		Rectangle bounds = screens[1].getDefaultConfiguration().getBounds();
		System.out.println("Second monitor bounds: " + bounds);

		// 2. Launch external program
		ProcessBuilder processBuilder = new ProcessBuilder(Constants.PROGRAM_COMMAND);
		processBuilder.directory(new java.io.File(Constants.PROGRAM_PATH)); // set the working directory
		Process process = processBuilder.start();

		// Wait a bit for the window to appear
		Thread.sleep(2000);

		// 3. Find the window by its title (for Notepad in English: "Untitled - Notepad")
		User32 user32 = User32.INSTANCE;
        HWND hwnd = user32.FindWindow(null, "Receitanet BX");
		if (hwnd == null) {
			System.out.println("Could not find window for process!");
			process.destroy();
			return;
		}
		
		// 3. Get its PID (Java 9+)
//		long pid = process.pid();
//		System.out.println("Launched program with PID: " + pid + ", INFO: " + process.info());
	       // 4. Find the window belonging to this PID
//        HWND[] foundWindow = new HWND[1];
//        user32.EnumWindows((hWnd, arg) -> {
//            IntByReference pidRef = new IntByReference();
//            user32.GetWindowThreadProcessId(hWnd, pidRef);
//            
//            System.out.println("pidRef: " + pidRef.getValue());
//            
//            if (pidRef.getValue() == pid && user32.IsWindowVisible(hWnd)) {
//                foundWindow[0] = hWnd;
//                return false; // stop enumeration
//            }
//            return true;
//        }, null);
//
//        if (foundWindow[0] == null) {
//            System.out.println("Could not find window for process!");
//            return;
//        }		

		// 5. Compute centered position on second monitor
		int windowWidth = 800;
		int windowHeight = 600;
		int x = bounds.x + (bounds.width - windowWidth) / 2;
		int y = bounds.y + (bounds.height - windowHeight) / 2;

		// 6. Move window
		boolean moved = user32.MoveWindow(hwnd, x, y, windowWidth, windowHeight, true);

		if (moved) {
			System.out.println("Window moved to second monitor and centered!");
		} else {
			System.out.println("Failed to move window.");
		}
	}

}
