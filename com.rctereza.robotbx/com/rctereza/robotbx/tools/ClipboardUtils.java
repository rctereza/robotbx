package com.rctereza.robotbx.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ClipboardUtils {

	public static void copyToClipboard(String text) {
		StringSelection selection = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
	}

	public static String readFromClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
				return (String) clipboard.getData(DataFlavor.stringFlavor);
			}
		} catch (UnsupportedFlavorException | java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void clearClipboard() {
		try {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents((Transferable) null, null);
		} catch (Exception ex) {
			// do nothing!
		}
	}

	public static boolean hasText() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		return clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
	}

}
