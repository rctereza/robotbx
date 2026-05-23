package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.Date;

import com.rctereza.robotbx.interfaces.Wrappable;

public record Procurator(String CERTIFICADO, String PROCURADOR, String PROCURADOR_DOC, String CLIENTE,
		String CLIENTE_DOC, Date VALIDADE) implements Serializable, Wrappable {

	public Procurator() {
		this(null, null, null, null, null, null);
	}

	@Override
	public Integer getObjectId() {
		return null;
	}

	@Override
	public String toString() {
		return CLIENTE;
	}
}
