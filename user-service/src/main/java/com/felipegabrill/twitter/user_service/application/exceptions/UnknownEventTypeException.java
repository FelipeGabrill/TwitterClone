package com.felipegabrill.twitter.user_service.application.exceptions;

public class UnknownEventTypeException extends RuntimeException {
    public UnknownEventTypeException(String message) {
        super(message);
    }
}
