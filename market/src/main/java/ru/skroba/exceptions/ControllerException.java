package ru.skroba.exceptions;

public class ControllerException extends RuntimeException {
    private final int code;
    public ControllerException(final int code, final String message) {
        super(message);
        this.code = code;
    }
    
    public ControllerException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public ControllerException(final int code, final Throwable cause) {
        super(cause);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
