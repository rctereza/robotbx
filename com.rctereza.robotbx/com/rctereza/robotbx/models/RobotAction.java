package com.rctereza.robotbx.models;

import java.util.List;

public record RobotAction(
	  Integer ID
	, String  DESCRIPTION
	, Boolean ENABLED
	, List<RobotCommand> ROBOT_COMMANDS
) {}
