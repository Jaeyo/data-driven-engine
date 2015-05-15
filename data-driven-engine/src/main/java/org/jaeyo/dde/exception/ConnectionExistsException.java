package org.jaeyo.dde.exception;

public class ConnectionExistsException extends Exception {

	public ConnectionExistsException() {
		super();
	}

	public ConnectionExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConnectionExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionExistsException(String message) {
		super(message);
	}

	public ConnectionExistsException(Throwable cause) {
		super(cause);
	}

}
