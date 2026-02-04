package com.felipegabrill.twitter.follow_service.service.follow.exceptions;

public class UserCannotFollowSelfException extends RuntimeException {
    public UserCannotFollowSelfException(String message) {
        super(message);
    }
}
