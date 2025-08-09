package com.parseme.exceptions;

public class ParseMeError extends RuntimeException {
    public ParseMeError(String message, Throwable cause) {
        super(message, cause);
    }
}
