package org.jaeyo.dde.exception;

public class AlreadyStoppedException extends Exception{

	public AlreadyStoppedException() {
		super();
	}

	public AlreadyStoppedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlreadyStoppedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyStoppedException(String message) {
		super(message);
	}

	public AlreadyStoppedException(Throwable cause) {
		super(cause);
	}

}
