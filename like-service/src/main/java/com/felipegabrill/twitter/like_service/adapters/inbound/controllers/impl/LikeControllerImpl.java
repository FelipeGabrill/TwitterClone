package com.felipegabrill.twitter.like_service.adapters.inbound.controllers.impl;

import com.felipegabrill.twitter.like_service.adapters.inbound.controllers.LikeController;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import com.felipegabrill.twitter.like_service.application.usecases.LikeUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LikeControllerImpl implements LikeController {

    private final LikeUseCases likeUseCases;

    public LikeControllerImpl(LikeUseCases likeUseCases) {
        this.likeUseCases = likeUseCases;
    }

    @Override
    public ResponseEntity<Void> like(UUID userId, UUID tweetId) {
        likeUseCases.like(userId, tweetId);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> unlike(UUID userId, UUID tweetId) {
        likeUseCases.unlike(userId, tweetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<LikeStatusResponseDTO> hasLiked(UUID userId, UUID tweetId) {
        return ResponseEntity.ok(likeUseCases.
                hasLiked(userId, tweetId));
    }

    @Override
    public ResponseEntity<Page<LikeResponseDTO>> listLikesByTweet(
            UUID tweetId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                likeUseCases.listLikesByTweetId(tweetId, pageable)
        );
    }
}
