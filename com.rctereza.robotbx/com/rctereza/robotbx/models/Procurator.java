package com.rctereza.robotbx.models;

import java.io.Serializable;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Procurator(String CERTIFICATE_NAME, String CUSTOMER_NAME, String CUSTOMER_DOC, String EXPIRE_DATE)
		implements Serializable, Cloneable, Wrappable {

	public Procurator() {
		this(null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}

	@Override
	public Procurator clone() {
		try {
			return (Procurator) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
