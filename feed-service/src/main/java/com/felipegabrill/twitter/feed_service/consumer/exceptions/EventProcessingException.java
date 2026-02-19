package com.felipegabrill.twitter.feed_service.consumer.exceptions;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(String message) {
        super(message);
    }
}
