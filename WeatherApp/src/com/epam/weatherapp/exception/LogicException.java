package com.epam.weatherapp.exception;

public class LogicException extends Exception {
	
	private static final long serialVersionUID = -7537568409607892642L;

	public LogicException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public LogicException(String detailMessage) {
		super(detailMessage);
	}

	public LogicException(Throwable cause) {
		super(cause);
	}
}
