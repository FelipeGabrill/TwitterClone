package com.felipegabrill.twitter.feed_service.consumer.exceptions;

public class UnknownEventTypeException extends EventProcessingException {
    public UnknownEventTypeException(String message) {
        super(message);
    }
}
