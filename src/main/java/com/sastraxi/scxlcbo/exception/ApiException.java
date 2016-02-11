package com.sastraxi.scxlcbo.exception;

/**
 * Represents an abnormal response from an API.
 */
public class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Exception cause) {
        super(message, cause);
    }
}
