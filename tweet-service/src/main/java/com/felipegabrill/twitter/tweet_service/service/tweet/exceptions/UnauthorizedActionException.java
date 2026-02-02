package com.felipegabrill.twitter.tweet_service.service.tweet.exceptions;

public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String msg) {
        super(msg);
    }
}