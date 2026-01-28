package com.felipegabrill.twitter.user_service.application.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}