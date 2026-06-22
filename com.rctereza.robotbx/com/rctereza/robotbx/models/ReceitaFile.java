package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.interfaces.Wrappable;

public record ReceitaFile(
		  String FILE_NAME
		, String FILE_PATH
		, String FILE_EXTENSION
		, String FILE_SHORT_NAME
		, String DOCUMENT_NAME
		, String DOCUMENT_PERIOD_FROM
		, String DOCUMENT_PERIOD_TO
		, String DOCUMENT_CODE
		, String DOCUMENT_TYPE
		, String DOCUMENT_DATETIME_SENT
		, String DOCUMENT_KEY)
		implements Serializable, Wrappable {

	public ReceitaFile() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}
}
