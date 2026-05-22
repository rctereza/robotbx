package com.rctereza.robotbx.ztest;

import java.lang.reflect.Method;

import com.rctereza.robotbx.tools.ScreenResolution;

public class CheckingMonitorResolution {

//	private static final Logger logger = LoggerFactory.getLogger(ProcessRobot.class);
	
	public static void main(String[] args) throws Exception {
		
		// Get method reference
        Method method = CheckingMonitorResolution.class.getDeclaredMethod("CheckMonitorResolution");

        // Allow access to private method
        method.setAccessible(true);

        // Invoke method
        Object valid = method.invoke(new CheckingMonitorResolution());

        // Convert returned value
        boolean value = (boolean) valid;

        if (value) 		
        	System.out.println("Done!");
        else
			System.out.println("Error...");
	}

	@SuppressWarnings("unused")
	private boolean CheckMonitorResolution() {
		return ScreenResolution.moveAppTo1920x1080Monitor();
	}
}
