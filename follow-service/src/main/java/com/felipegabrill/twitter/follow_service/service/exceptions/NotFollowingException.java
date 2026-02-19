package com.felipegabrill.twitter.follow_service.service.exceptions;

public class NotFollowingException extends RuntimeException {
    public NotFollowingException(String message) {
        super(message);
    }
}
