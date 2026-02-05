package com.felipegabrill.twitter.like_service.application.exceptions;

public class LikeAlreadyExistsException extends RuntimeException {
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
