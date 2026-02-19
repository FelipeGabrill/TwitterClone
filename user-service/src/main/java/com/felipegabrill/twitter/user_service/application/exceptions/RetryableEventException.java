package com.felipegabrill.twitter.user_service.application.exceptions;

public class RetryableEventException extends RuntimeException {
    public RetryableEventException(String message) {
        super(message);
    }
}
