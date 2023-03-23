package ru.skroba.exceptions;

public class ServiceException extends Exception {
    public ServiceException(final String message) {
        super(message);
    }
    
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
