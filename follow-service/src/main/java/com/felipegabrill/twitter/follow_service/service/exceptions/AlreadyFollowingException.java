package com.felipegabrill.twitter.follow_service.service.exceptions;

public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException(String message) {
        super(message);
    }
}
