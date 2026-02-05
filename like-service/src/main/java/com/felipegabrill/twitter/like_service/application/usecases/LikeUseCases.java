package com.felipegabrill.twitter.like_service.application.usecases;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LikeUseCases {

    void like(UUID userId, UUID tweetId);

    void unlike(UUID userId, UUID tweetId);

    LikeStatusResponseDTO hasLiked(UUID userId, UUID tweetId);

    Page<LikeResponseDTO> listLikesByTweetId(UUID tweetId, Pageable pageable);

}
