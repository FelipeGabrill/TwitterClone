package com.felipegabrill.twitter.feed_service.consumer.exceptions;

public class InvalidEventException extends EventProcessingException {
    public InvalidEventException(String message) {
        super(message);
    }
}
