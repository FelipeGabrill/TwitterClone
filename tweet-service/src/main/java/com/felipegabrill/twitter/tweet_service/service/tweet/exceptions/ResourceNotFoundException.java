package com.felipegabrill.twitter.tweet_service.service.tweet.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
