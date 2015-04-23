package org.jaeyo.dde.exception;

public class UnknownComponentException extends Exception {

	public UnknownComponentException() {
		super();
	}

	public UnknownComponentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnknownComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownComponentException(String message) {
		super(message);
	}

	public UnknownComponentException(Throwable cause) {
		super(cause);
	}
}