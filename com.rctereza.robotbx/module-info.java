module com.rctereza.robotbx {
	requires java.base;
	requires java.prefs;
	requires java.datatransfer;

	requires transitive java.desktop;
	requires transitive com.sun.jna.platform;
	requires transitive com.sun.jna;

	requires com.formdev.flatlaf;
	requires com.formdev.flatlaf.extras;
	requires com.formdev.flatlaf.fonts.roboto;

	requires com.miglayout.swing;
	requires com.github.weisj.jsvg;
	requires com.rctereza.robotocr;
	requires org.slf4j;
	
	exports com.rctereza.robotbx.enums;
	exports com.rctereza.robotbx.models;
	exports com.rctereza.robotbx.tools;
	exports com.rctereza.robotbx.ztest;
}