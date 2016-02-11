package com.sastraxi.scxlcbo.exception;

/**
 * Thrown when we cannot communicate with the Database for any reason.
 */
public class DatabaseException extends Exception {

    public DatabaseException(String message) {
        super(message);
    }

}
