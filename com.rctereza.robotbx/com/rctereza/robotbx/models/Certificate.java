package com.rctereza.robotbx.models;

import java.io.Serializable;
import java.util.Date;

public record Certificate(
	  Integer ID
	, String  NAME
	, String  PATH
	, String  PASS
	, String  ALIAS
	, String  SUBJECT
	, String  ISSUER
	, Date    VALIDFROM
	, Date    VALIDTO
) 
implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Certificate() {
		this(null,null,null,null,null,null,null,null,null);
	}

	@Override
	public String toString() {
		return PATH + "\\" + NAME;
	}
	
}
