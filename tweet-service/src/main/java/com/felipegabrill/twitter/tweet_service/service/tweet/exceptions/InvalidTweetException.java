package com.felipegabrill.twitter.tweet_service.service.tweet.exceptions;


public class InvalidTweetException extends RuntimeException {

    public InvalidTweetException(String msg) {
        super(msg);
    }
}
