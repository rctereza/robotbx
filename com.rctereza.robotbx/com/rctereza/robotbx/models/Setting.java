package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Setting(
		  String  SOFTWARE_NAME
		, String  SOFTWARE_PATH
		, String  SOFTWARE_PROGRAM
		, String  DOWNLOAD_FOLDER
		, String  LOG_FOLDER
		, String  SAVE_FOLDER
		, Boolean MAKE_SUBFOLDER
		, Boolean AUTO_DOWNLOAD
		, Integer NUMBER_DOWNLOAD_SIMULTANEOUS
		, Integer MINUTES_FOR_NEXT_ORDER_UPDATE
		, KeepWhichFiles KEEP_WHICH_FILES
		)
		implements Serializable, Wrappable {

	public Setting() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public enum KeepWhichFiles {
        ALL, ONLY_AMEND
    }

	@Override
	public Integer getObjectId() {
		return null;
	}
}
