package com.rctereza.robotbx.tools;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public class ListWindows {

	public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.load("user32", User32.class);

        int EnumWindows(WndProc wndProc, Pointer arg);
        boolean GetWindowRect(int hwnd, RECT rect);

        interface WndProc extends Callback {
            boolean invoke(int hwnd, Pointer arg);
        }
    }

    public static class RECT extends Structure {
        public int left;
        public int top;
        public int right;
        public int bottom;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("left", "top", "right", "bottom");
        }
    }

    public static void main(String[] args) {
        User32.INSTANCE.EnumWindows(new User32.WndProc() {
            @Override
            public boolean invoke(int hwnd, Pointer arg) {
                RECT rect = new RECT();
                if (User32.INSTANCE.GetWindowRect(hwnd, rect)) {
                    // Output window dimensions
                    int width = rect.right - rect.left;
                    int height = rect.bottom - rect.top;

                    System.out.println("Window Handle: " + hwnd);
                    System.out.println("Width: " + width + ", Height: " + height);
                }
                return true;  // Continue enumerating
            }
        }, null);
    }


}
