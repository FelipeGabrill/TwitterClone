package com.felipegabrill.twitter.feed_service.services;

import com.felipegabrill.twitter.feed_service.dtos.response.FeedResponseDTO;

import java.time.Instant;
import java.util.UUID;


public interface IFeedService {

    /**
     * Retrieves a user's personalized feed.
     *
     * @param userId         the ID of the user whose feed is being retrieved
     * @param limit          the maximum number of feed items to return
     * @param lastRdsKeyStr  the cursor/key for the last retrieved item in RDS (for pagination)
     * @param lastDynamoKeyStr the cursor/key for the last retrieved item in DynamoDB (for pagination)
     * @return a FeedResponseDTO containing the user's feed items
     */
    FeedResponseDTO getUserFeed(String userId, Integer limit, String lastRdsKeyStr, String lastDynamoKeyStr);

    /**
     * Retrieves the global feed containing tweets from all users.
     *
     * @param limit   the maximum number of feed items to return
     * @param lastKey the cursor/key for the last retrieved item (for pagination)
     * @return a FeedResponseDTO containing the global feed items
     */
    FeedResponseDTO getGlobalFeed(Integer limit, String lastKey);

    /**
     * Handles a new tweet event, updating feeds or triggering downstream processing.
     *
     * @param tweetId    the unique identifier of the new tweet
     * @param tweetType  the type of the tweet (e.g., NORMAL, REPLY, RETWEET, QUOTE)
     * @param authorId   the ID of the user who authored the tweet
     * @param rootTweetId the ID of the original tweet if this is a reply, retweet, or quote; null otherwise
     * @param occurredAt the timestamp when the tweet event occurred
     */
    void handleNewTweet(UUID tweetId,
                        String tweetType,
                        UUID authorId,
                        UUID rootTweetId,
                        Instant occurredAt);
}
