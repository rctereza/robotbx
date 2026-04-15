package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.enums.Command;

public record RobotTextCommand(
	  Integer ID
	, Command COMMAND
	, String TEXT
	, Boolean ENABLED
) 
implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public RobotTextCommand() {
		this(null,null,null,null);
	}
}
