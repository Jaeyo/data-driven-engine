package org.jaeyo.dde.exception;

public class UnknownConfigException extends Exception {

	public UnknownConfigException() {
		super();
	}

	public UnknownConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnknownConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownConfigException(String message) {
		super(message);
	}

	public UnknownConfigException(Throwable cause) {
		super(cause);
	}

}
