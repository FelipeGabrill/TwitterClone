package com.felipegabrill.twitter.tweet_service.consumer.exceptions;

public class NonRetryableEventException extends RuntimeException {
    public NonRetryableEventException(String message) {
        super(message);
    }
}
