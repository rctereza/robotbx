package com.rctereza.robotbx.models;

import java.util.List;

public record RobotAction(
	  Integer ID
	, String  DESCRIPTION
	, Boolean MESSAGEBOX
	, List<RobotMessageBox> MESSAGEBOX_TEXTS
	, Boolean WAIT
	, Integer WAIT_MILLISECONDS
	, Integer NUMBER_OF_ATTEMPTS
	, Boolean CONFIRMATON
	, Boolean LAST_ACTION
	, Boolean ENABLED
	, List<RobotCommand> ROBOT_COMMANDS
) {}
