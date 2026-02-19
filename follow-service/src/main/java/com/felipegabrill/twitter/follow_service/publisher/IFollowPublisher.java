package com.felipegabrill.twitter.follow_service.publisher;

import java.util.UUID;

public interface IFollowPublisher {

    /**
     * Publishes an event indicating that a user has followed another user.
     *
     * @param followerId the ID of the user who followed
     * @param followedId the ID of the user who was followed
     */
    void publishUserFollowed(UUID followerId, UUID followedId);

    /**
     * Publishes an event indicating that a user has unfollowed another user.
     *
     * @param followerId   the ID of the user who unfollowed
     * @param unfollowedId the ID of the user who was unfollowed
     */
    void publishUserUnfollowed(UUID followerId, UUID unfollowedId);
}
