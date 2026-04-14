package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.Map;

public record RobotTexts(  
      Integer ID
	, String  NAME
	, Boolean ENABLED
	, Map<Integer, String> TEXTS
) 
implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public RobotTexts() {
		this(null,null,null,null);
	}
	
}