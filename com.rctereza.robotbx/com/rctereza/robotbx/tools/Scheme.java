package com.rctereza.robotbx.tools;

import java.util.prefs.Preferences;

import com.rctereza.robotbx.Constants;

public class Scheme {
	
	public static Boolean isLafDark() {
		Preferences prefs = Preferences.userNodeForPackage(Scheme.class);
		return !prefs.get(Constants.SCHEME_THEME, "").isEmpty();
	}

	public static void setLafDark() {
		Preferences prefs = Preferences.userNodeForPackage(Scheme.class);
		prefs.put(Constants.SCHEME_THEME, "YES");
	}

	public static void removeLafDark() {
		Preferences prefs = Preferences.userNodeForPackage(Scheme.class);
		prefs.put(Constants.SCHEME_THEME, "");
	}

}

