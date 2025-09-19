module com.rctereza.robotbx {
	requires java.desktop;

	requires com.miglayout.swing;

	requires com.formdev.flatlaf;
	requires com.formdev.flatlaf.extras;
	requires com.formdev.flatlaf.fonts.roboto;

	requires transitive com.github.weisj.jsvg;
	
	requires transitive com.sun.jna.platform;
	requires transitive com.sun.jna;
	
	exports com.rctereza.robotbx.models;
	exports com.rctereza.robotbx.tools;
	exports com.rctereza.robotbx.ztest;
}