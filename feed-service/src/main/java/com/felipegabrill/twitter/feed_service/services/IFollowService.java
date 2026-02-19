package com.felipegabrill.twitter.feed_service.services;

import java.util.List;
import java.util.UUID;

public interface IFollowService {

    /**
     * Follows a user on behalf of another user.
     *
     * @param followerId the ID of the user who wants to follow
     * @param followedId the ID of the user to be followed
     */
    void followUser(UUID followerId, UUID followedId);

    /**
     * Unfollows a user on behalf of another user.
     *
     * @param followerId the ID of the user who wants to unfollow
     * @param followedId the ID of the user to be unfollowed
     */
    void unfollowUser(UUID followerId, UUID followedId);

    /**
     * Retrieves a list of user IDs that the given user is following.
     *
     * @param userId the ID of the user whose followed users are being retrieved
     * @return a list of user IDs that the user is following
     */
    List<String> getFollowedUsers(UUID userId);
}
