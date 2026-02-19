package com.felipegabrill.twitter.follow_service.service.exceptions;

public class UserCannotFollowSelfException extends RuntimeException {
    public UserCannotFollowSelfException(String message) {
        super(message);
    }
}
