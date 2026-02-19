package com.felipegabrill.twitter.feed_service.services;

import java.util.UUID;

public interface IFamousUserService {

    /**
     * Promotes a user to a "famous" status based on their follower count.
     *
     * @param userId        the ID of the user to promote
     * @param followerCount the current number of followers of the user
     */
    void promoteToFamous(UUID userId, long followerCount);

    /**
     * Checks if a user has a "famous" status.
     *
     * @param userId the ID of the user to check
     * @return true if the user is considered famous, false otherwise
     */
    boolean isUserFamous(UUID userId);
}
