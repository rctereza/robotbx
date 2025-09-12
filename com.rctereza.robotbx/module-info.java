module com.rctereza.robotbx {
	requires java.desktop;

	requires com.sun.jna.platform;
	
	requires transitive com.sun.jna;
	
	exports com.rctereza.robotbx.models;
	exports com.rctereza.robotbx.tools;
}