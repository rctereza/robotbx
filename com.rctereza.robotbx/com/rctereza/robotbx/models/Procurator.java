package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Procurator(String CERTIFICATE_NAME, String CUSTOMER_NAME, String CUSTOMER_DOC, String EXPIRE_DATE)
		implements Serializable, Wrappable {

	public Procurator() {
		this(null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}
}
