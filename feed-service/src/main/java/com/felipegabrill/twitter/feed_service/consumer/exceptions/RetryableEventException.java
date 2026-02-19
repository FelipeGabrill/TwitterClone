package com.felipegabrill.twitter.feed_service.consumer.exceptions;

public class RetryableEventException extends EventProcessingException {
    public RetryableEventException(String message) {
        super(message);
    }
}
