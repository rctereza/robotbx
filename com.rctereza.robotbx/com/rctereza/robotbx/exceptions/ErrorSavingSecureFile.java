package com.rctereza.robotbx.exceptions;

public class ErrorSavingSecureFile extends Exception {

	private static final long serialVersionUID = 7921713137387070721L;

	public ErrorSavingSecureFile() {
		super();
	}

	public ErrorSavingSecureFile(String message) {
		super(message);
	}

	public ErrorSavingSecureFile(String message, Throwable cause) {
		super(message, cause);
	}

}
