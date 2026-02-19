package com.felipegabrill.twitter.like_service.adapters.inbound.controllers.impl;

import com.felipegabrill.twitter.like_service.adapters.inbound.controllers.LikeController;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import com.felipegabrill.twitter.like_service.application.usecases.LikeUseCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LikeControllerImpl implements LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeControllerImpl.class);

    private final LikeUseCases likeUseCases;

    public LikeControllerImpl(LikeUseCases likeUseCases) {
        this.likeUseCases = likeUseCases;
    }

    @Override
    public ResponseEntity<Void> like(UUID userId, UUID tweetId) {
        logger.info(
                "HTTP POST /like requested. userId={}, tweetId={}",
                userId, tweetId
        );

        likeUseCases.like(userId, tweetId);

        logger.info(
                "Tweet liked successfully. userId={}, tweetId={}",
                userId, tweetId
        );

        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> unlike(UUID userId, UUID tweetId) {
        logger.info(
                "HTTP DELETE /like requested. userId={}, tweetId={}",
                userId, tweetId
        );

        likeUseCases.unlike(userId, tweetId);

        logger.info(
                "Tweet unliked successfully. userId={}, tweetId={}",
                userId, tweetId
        );

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<LikeStatusResponseDTO> hasLiked(UUID userId, UUID tweetId) {
        logger.info(
                "HTTP GET /like/status requested. userId={}, tweetId={}",
                userId, tweetId
        );

        LikeStatusResponseDTO response = likeUseCases.hasLiked(userId, tweetId);

        logger.debug(
                "Like status retrieved. userId={}, tweetId={}, liked={}",
                userId, tweetId, response.isLiked()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<LikeResponseDTO>> listLikesByTweet(
            UUID tweetId,
            Pageable pageable
    ) {
        logger.info(
                "HTTP GET /likes by tweet requested. tweetId={}, page={}, size={}",
                tweetId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<LikeResponseDTO> response =
                likeUseCases.listLikesByTweetId(tweetId, pageable);

        logger.info(
                "Likes retrieved successfully. tweetId={}, totalElements={}",
                tweetId, response.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }
}
