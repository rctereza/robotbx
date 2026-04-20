package com.rctereza.robotbx.models;

import java.util.List;

import com.rctereza.robotbx.enums.Message;

public record RobotMessageBox(
	  Integer ID
	, String  MESSAGE
	, String  RESPONSE
	, Boolean ABORT
	, Message TYPE
	, List<RobotCommand> COMMANDS
) {}
