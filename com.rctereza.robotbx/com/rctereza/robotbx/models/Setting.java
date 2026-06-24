package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Setting(
		  String  SOFTWARE_NAME
		, String  SOFTWARE_PATH
		, String  SOFTWARE_PROGRAM
		, String  DOWNLOAD_FOLDER
		, String  SAVE_FOLDER
		, String  LOG_FOLDER
		, Boolean SAVE_LOG
		, Boolean MAKE_SUBFOLDER
		, Boolean AUTO_DOWNLOAD
		, Integer NUMBER_DOWNLOAD_SIMULTANEOUS
		, Integer MINUTES_FOR_NEXT_ORDER_UPDATE
		, KeepWhichFiles KEEP_WHICH_FILES
		, Boolean DATA_UPDATED
		)
		implements Serializable, Wrappable {

	public Setting() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public enum KeepWhichFiles {
        ALL, ONLY_AMEND
    }

	public Integer getObjectId() {
		return null;
	}
	
}
