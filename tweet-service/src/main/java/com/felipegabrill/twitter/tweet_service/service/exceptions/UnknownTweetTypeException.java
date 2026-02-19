package com.felipegabrill.twitter.tweet_service.service.exceptions;

public class UnknownTweetTypeException extends RuntimeException {
    public UnknownTweetTypeException(String message) {
        super(message);
    }
}
