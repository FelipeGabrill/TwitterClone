package com.felipegabrill.twitter.tweet_service.service.tweet.exceptions;

public class UnknownTweetTypeException extends RuntimeException {
    public UnknownTweetTypeException(String message) {
        super(message);
    }
}
