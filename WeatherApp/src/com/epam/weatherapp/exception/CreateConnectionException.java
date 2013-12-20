package com.epam.weatherapp.exception;

public final class CreateConnectionException extends ReadWebPageException {

	private static final long serialVersionUID = 3933692354734294422L;

	public CreateConnectionException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public CreateConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public CreateConnectionException(Throwable cause) {
		super(cause);
	}
	
}
