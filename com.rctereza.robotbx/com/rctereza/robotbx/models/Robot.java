package com.rctereza.robotbx.models;

import java.util.List;

public record Robot(
	  Integer ID
	, String  NAME
	, Integer SCREEN_HEIGHT
	, Integer SCREEN_WIDTH
	, Integer WINDOW_HEIGHT
	, Integer WINDOW_WIDTH
	, Boolean ENABLED
	, List<RobotAction> ROBOT_ACTIONS
) 
{
	public Robot() {
		this(null,null,null,null,null,null,null,null);
	}
}
