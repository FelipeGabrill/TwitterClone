package com.felipegabrill.twitter.like_service.application.publisher;

import java.util.UUID;

public interface ILikePublisher {

    /**
     * Publishes an event indicating that a user has liked a tweet.
     *
     * @param tweetId the ID of the tweet that was liked
     * @param userId  the ID of the user who liked the tweet
     */
    void publishLikeCreated(UUID tweetId, UUID userId);

    /**
     * Publishes an event indicating that a user has removed a like from a tweet.
     *
     * @param tweetId the ID of the tweet that was unliked
     * @param userId  the ID of the user who removed the like
     */
    void publishLikeDeleted(UUID tweetId, UUID userId);
}
