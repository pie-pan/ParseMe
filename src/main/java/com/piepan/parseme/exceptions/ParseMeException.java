package com.piepan.parseme.exceptions;

public class ParseMeException extends RuntimeException {
    public ParseMeException(String message, Throwable cause) {
        super(message, cause);
    }
    public ParseMeException(String message) {
        super(message);
    }
}
