package com.felipegabrill.twitter.feed_service.services.exceptions;

public class InvalidFollowException extends RuntimeException {
    public InvalidFollowException(String message) {
        super(message);
    }
}
