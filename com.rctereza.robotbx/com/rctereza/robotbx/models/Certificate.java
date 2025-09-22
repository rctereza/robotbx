package com.rctereza.robotbx.models;

public record Certificate(
	  Integer ID
	, String  FILE_NAME
	, String  FILE_PATH
	, String  FILE_PASS
	, String  CUSTOMER
) {

	@Override
	public String toString() {
		return FILE_NAME;
	}
	
	public String toString2() {
		return "Certificate [ID=" + ID + ", FILE_NAME=" + FILE_NAME + ", FILE_PATH=" + FILE_PATH + ", FILE_PASS="
				+ FILE_PASS + ", CUSTOMER=" + CUSTOMER + "]";
	}
}
