package com.rctereza.robotbx.ztest;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class WindowStyleExample {

	private static final int GWL_STYLE = -16;
    private static final int GWL_EXSTYLE = -20;

    public static void main(String[] args) {
        User32 user32 = User32.INSTANCE;

        // Example: find a window by title (e.g., Notepad)
        HWND hWnd = user32.FindWindow(null, "Receitanet BX");
        if (hWnd == null) {
            System.out.println("Window not found.");
            return;
        }

        char[] buffer = new char[512];
        user32.GetWindowText(hWnd, buffer, 512);
        System.out.println("Window title: " + Native.toString(buffer));
        
    	user32.EnumChildWindows(hWnd, (child, d) -> {
			user32.GetClassName(child, buffer, 512);
			String className = Native.toString(buffer).trim();
			System.out.println("Class Name: " + className);
			return true;
		}, Pointer.NULL);
        
        int style = user32.GetWindowLong(hWnd, GWL_STYLE);
        int exStyle = user32.GetWindowLong(hWnd, GWL_EXSTYLE);

        System.out.printf("HWND=%s\n", hWnd);
        System.out.printf("Style (GWL_STYLE): 0x%08X\n", style);
        System.out.printf("Extended Style (GWL_EXSTYLE): 0x%08X\n", exStyle);

        // Example: check for specific style flags
        final int WS_VISIBLE = 0x10000000;
        final int WS_POPUP   = 0x80000000;
        final int WS_CAPTION = 0x00C00000;

        if ((style & WS_VISIBLE) != 0) {
            System.out.println(" → Window is visible");
        }
        if ((style & WS_POPUP) != 0) {
            System.out.println(" → Window is a popup");
        }
        if ((style & WS_CAPTION) != 0) {
            System.out.println(" → Window has a caption bar");
        }
    }

    /*
	HWND=native@0x605a0
	Style (GWL_STYLE): 0x16CF0000
	Extended Style (GWL_EXSTYLE): 0x00000100
	→ Window is visible
	→ Window has a caption bar
	------------------------------------------------------------------------
	HWND=native@0x14045a
	Style (GWL_STYLE): 0x96C80000
	Extended Style (GWL_EXSTYLE): 0x00000101
	 → Window is visible
	 → Window is a popup
	 → Window has a caption bar
	 	
	*/

}
