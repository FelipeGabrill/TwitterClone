package com.felipegabrill.twitter.tweet_service.publisher;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;

import java.util.UUID;

public interface ITweetPublisher {

    /**
     * Sends a tweet event message to the messaging system.
     *
     * @param tweetId     the unique identifier of the tweet
     * @param authorId    the ID of the user who authored the tweet
     * @param tweetType   the type of the tweet (e.g., NORMAL, REPLY, RETWEET, QUOTE)
     * @param rootTweetId the ID of the original tweet if this is a reply, retweet, or quote; null otherwise
     */
    void sendMessage(UUID tweetId, UUID authorId, TweetType tweetType, UUID rootTweetId);
}
