package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.List;

public record RobotText(  
      Integer ID
	, String  NAME
	, Boolean ENABLED
	, List<RobotTextAction> ACTIONS
) 
implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public RobotText() {
		this(null,null,null,null);
	}
	
}