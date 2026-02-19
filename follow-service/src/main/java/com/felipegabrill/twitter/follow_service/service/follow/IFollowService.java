package com.felipegabrill.twitter.follow_service.service.follow;

import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IFollowService {

    /**
     * Follows a user on behalf of another user.
     *
     * @param followerId  the ID of the user who wants to follow
     * @param followingId the ID of the user to be followed
     * @return a FollowResponseDTO representing the follow relationship
     */
    FollowResponseDTO followUser(UUID followerId, UUID followingId);

    /**
     * Unfollows a user on behalf of another user.
     *
     * @param followerId  the ID of the user who wants to unfollow
     * @param followingId the ID of the user to be unfollowed
     */
    void unfollowUser(UUID followerId, UUID followingId);

    /**
     * Retrieves a paginated list of users that a given user is following.
     *
     * @param followerId the ID of the user whose following list is being retrieved
     * @param pageable   pagination information
     * @return a page of FollowResponseDTO representing the users being followed
     */
    Page<FollowResponseDTO> getFollowing(UUID followerId, Pageable pageable);

    /**
     * Retrieves a paginated list of followers for a given user.
     *
     * @param followingId the ID of the user whose followers are being retrieved
     * @param pageable    pagination information
     * @return a page of FollowResponseDTO representing the followers
     */
    Page<FollowResponseDTO> getFollowers(UUID followingId, Pageable pageable);

    /**
     * Checks if a user is following another user.
     *
     * @param followerId  the ID of the user who might be following
     * @param followingId the ID of the user who might be followed
     * @return true if followerId follows followingId, false otherwise
     */
    boolean isFollowing(UUID followerId, UUID followingId);
}

