package com.rctereza.robotbx.models;

import java.io.Serializable;

public record Certificate(
	  Integer ID
	, String  FILE_NAME
	, String  FILE_PATH
	, String  FILE_PASS
	, String  CUSTOMER
) implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Certificate() {
		this(null,null,null,null,null);
	}

	@Override
	public String toString() {
		return FILE_NAME;
	}
	
	public String toString2() {
		return "Certificate [ID=" + ID + ", FILE_NAME=" + FILE_NAME + ", FILE_PATH=" + FILE_PATH + ", FILE_PASS="
				+ FILE_PASS + ", CUSTOMER=" + CUSTOMER + "]";
	}
}
