package com.felipegabrill.twitter.user_service.application.exceptions;

public class NonRetryableEventException extends RuntimeException {
    public NonRetryableEventException(String message) {
        super(message);
    }
}
