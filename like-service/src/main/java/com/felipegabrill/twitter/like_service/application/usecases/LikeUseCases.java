package com.felipegabrill.twitter.like_service.application.usecases;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LikeUseCases {

    /**
     * Likes a tweet on behalf of a user.
     *
     * @param userId  the ID of the user who is liking the tweet
     * @param tweetId the ID of the tweet to like
     */
    void like(UUID userId, UUID tweetId);

    /**
     * Removes a like from a tweet on behalf of a user.
     *
     * @param userId  the ID of the user who is unliking the tweet
     * @param tweetId the ID of the tweet to unlike
     */
    void unlike(UUID userId, UUID tweetId);

    /**
     * Checks if a user has liked a specific tweet.
     *
     * @param userId  the ID of the user
     * @param tweetId the ID of the tweet
     * @return a LikeStatusResponseDTO indicating whether the user has liked the tweet
     */
    LikeStatusResponseDTO hasLiked(UUID userId, UUID tweetId);

    /**
     * Retrieves a paginated list of likes for a given tweet.
     *
     * @param tweetId  the ID of the tweet
     * @param pageable pagination information
     * @return a page of LikeResponseDTO representing the likes on the tweet
     */
    Page<LikeResponseDTO> listLikesByTweetId(UUID tweetId, Pageable pageable);
}

