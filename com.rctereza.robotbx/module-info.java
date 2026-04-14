module com.rctereza.robotbx {
	requires transitive java.desktop;
	requires java.prefs;

	requires com.miglayout.swing;

	requires com.formdev.flatlaf;
	requires com.formdev.flatlaf.extras;
	requires com.formdev.flatlaf.fonts.roboto;

	requires com.github.weisj.jsvg;
	
	requires transitive com.sun.jna.platform;
	requires transitive com.sun.jna;
	requires com.rctereza.robotocr;
	
	exports com.rctereza.robotbx.enums;
	exports com.rctereza.robotbx.models;
	exports com.rctereza.robotbx.tools;
	exports com.rctereza.robotbx.ztest;
}