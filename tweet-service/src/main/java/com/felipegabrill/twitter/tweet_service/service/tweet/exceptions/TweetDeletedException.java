package com.felipegabrill.twitter.tweet_service.service.tweet.exceptions;

public class TweetDeletedException extends RuntimeException {

    public TweetDeletedException(String msg) {
        super(msg);
    }
}