package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.List;

public record RobotTextAction(
	  Integer ID
	, String  TEXT
	, Double SCALE
	, Boolean ENABLED
	, List<RobotTextCommand> COMMANDS
) 
implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public RobotTextAction() {
		this(null,null,null,null,null);
	}
}
