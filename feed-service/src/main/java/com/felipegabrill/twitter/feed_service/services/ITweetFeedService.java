package com.felipegabrill.twitter.feed_service.services;

import com.felipegabrill.twitter.feed_service.database.rds.entities.TweetEntity;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.result.PageResultTweet;
import com.felipegabrill.twitter.feed_service.infrastructure.util.TweetCursor;

import java.time.Instant;
import java.util.UUID;

public interface ITweetFeedService {

    /**
     * Processes a tweet event, typically to update feeds or trigger downstream actions.
     *
     * @param tweetId    the unique identifier of the tweet
     * @param authorId   the ID of the user who authored the tweet
     * @param occurredAt the timestamp when the tweet event occurred
     */
    void processTweetEvent(UUID tweetId,
                           UUID authorId,
                           Instant occurredAt);

    /**
     * Retrieves a paginated list of recent tweets from famous users for a given user.
     *
     * @param userId     the ID of the user requesting the feed
     * @param limit      the maximum number of tweets to return
     * @param lastCursor the cursor representing the last retrieved tweet (for pagination)
     * @return a PageResultTweet containing the recent famous tweets
     */
    PageResultTweet<TweetEntity> getRecentFamousTweets(
            UUID userId,
            int limit,
            TweetCursor lastCursor
    );
}
