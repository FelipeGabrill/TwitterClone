package com.felipegabrill.twitter.feed_service.consumer.exceptions;

public class NonRetryableEventException extends EventProcessingException {
    public NonRetryableEventException(String message) {
        super(message);
    }
}
