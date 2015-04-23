package org.jaeyo.dde.exception;

public class InvalidOperationException extends Exception {

	public InvalidOperationException() {
		super();
	}

	public InvalidOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOperationException(String message) {
		super(message);
	}

	public InvalidOperationException(Throwable cause) {
		super(cause);
	}

}
