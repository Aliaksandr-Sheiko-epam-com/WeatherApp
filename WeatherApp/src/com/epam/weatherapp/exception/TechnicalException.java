package com.epam.weatherapp.exception;

public class TechnicalException extends Exception {

    private static final long serialVersionUID = 4472049474129449509L;

    public TechnicalException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public TechnicalException(String detailMessage) {
        super(detailMessage);
    }

    public TechnicalException(Throwable cause) {
        super(cause);
    }
}
