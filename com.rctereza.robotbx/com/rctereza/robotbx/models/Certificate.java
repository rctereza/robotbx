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
	
	public Certificate() {
		this(null,null,null,null,null,null,null,null,null);
	}

	public String getAbsolutePath() {
		return PATH + "\\" + NAME;
	}

	@Override
	public String toString() {
		return NAME;
	}
	
}
