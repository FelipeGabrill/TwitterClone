package com.felipegabrill.twitter.user_service.application.publisher;

import java.util.UUID;

public interface IUserPublisher {

    /**
     * Sends an event when a user reaches a high number of followers.
     *
     * @param userId         the unique identifier of the user
     * @param followersCount the current number of followers of the user
     */
    void sendHighFollowersEvent(UUID userId, long followersCount);}
