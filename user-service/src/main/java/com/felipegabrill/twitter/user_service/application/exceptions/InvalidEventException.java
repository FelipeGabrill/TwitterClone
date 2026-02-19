package com.felipegabrill.twitter.user_service.application.exceptions;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String message) {
        super(message);
    }
}
