package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.Date;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Certificate(Integer ID, String NAME, String PATH, String PASS, String ALIAS, String SUBJECT,
		String ISSUER, Date VALIDFROM, Date VALIDTO, String CN, String CNDOC) implements Serializable, Wrappable {

	public Certificate() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}

	public String getAbsolutePath() {
		return PATH + "\\" + NAME;
	}

	@Override
	public String toString() {
		return NAME;
	}
}
