module com.rctereza.robotbx {
	requires java.desktop;

	requires com.formdev.flatlaf;
	requires com.formdev.flatlaf.extras;

	requires transitive com.sun.jna.platform;
	requires transitive com.sun.jna;
	
	exports com.rctereza.robotbx.models;
	exports com.rctereza.robotbx.tools;
	exports com.rctereza.robotbx.ztest;
}