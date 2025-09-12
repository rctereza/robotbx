package com.rctereza.robotbx.tools;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class PopupReader {

	public static void main(String[] args) {
		User32 user32 = User32.INSTANCE;

        // Example: find a window with a specific title
        HWND hWnd = user32.FindWindow(null, "Receitanet BX"); // Window title here
        if (hWnd != null) {
            char[] buffer = new char[512];
            user32.GetWindowText(hWnd, buffer, 512);
            System.out.println("Window title: " + Native.toString(buffer));

            // If the popup has a Static control (the message)
            HWND child2 = user32.FindWindowEx(hWnd, null, "Edit", null);
            if (child2 != null) {
                user32.GetWindowText(child2, buffer, 512);
                System.out.println("Popup message: " + Native.toString(buffer));
            }
            
            user32.EnumChildWindows(hWnd, (child, d) -> {
                char[] classBuf = new char[512];
                user32.GetClassName(child, classBuf, 512);
                String className = Native.toString(classBuf).trim();

                if ("Static".equals(className)) {
                    char[] textBuf = new char[512];
                    user32.GetWindowText(child, textBuf, 512);
                    String message = Native.toString(textBuf).trim();
                    if (!message.isEmpty()) {
                        System.out.println("   Message: " + message);
                    }
                }
                return true;
            }, Pointer.NULL);            
            
            
            
        } else {
            System.out.println("Popup not found.");
        }
	}

}
