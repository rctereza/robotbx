package com.rctereza.robotbx.models;

import com.rctereza.robotbx.enums.Command;

public record RobotCommand(
	  Integer ID
	, Command COMMAND
	, Integer VALUEX
	, Integer VALUEY
	, Integer WAITMS
	, String TEXT
	, Boolean ENABLED
) {}
