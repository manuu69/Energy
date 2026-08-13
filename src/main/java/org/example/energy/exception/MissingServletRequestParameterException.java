package org.example.energy.exception;

public class MissingServletRequestParameterException extends RuntimeException {
    public MissingServletRequestParameterException(String message) {
        super(message);
    }
}
