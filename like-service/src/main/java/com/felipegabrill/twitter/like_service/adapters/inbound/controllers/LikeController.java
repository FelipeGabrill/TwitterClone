package com.felipegabrill.twitter.like_service.adapters.inbound.controllers;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/likes")
public interface LikeController {

    @PostMapping("/users/{userId}/tweets/{tweetId}")
    ResponseEntity<Void> like(
            @PathVariable UUID userId,
            @PathVariable UUID tweetId
    );

    @DeleteMapping("/users/{userId}/tweets/{tweetId}")
    ResponseEntity<Void> unlike(
            @PathVariable UUID userId,
            @PathVariable UUID tweetId
    );

    @GetMapping("/users/{userId}/tweets/{tweetId}/has-liked")
    ResponseEntity<LikeStatusResponseDTO> hasLiked(
            @PathVariable UUID userId,
            @PathVariable UUID tweetId
    );

    @GetMapping("/tweets/{tweetId}")
    ResponseEntity<Page<LikeResponseDTO>> listLikesByTweet(
            @PathVariable UUID tweetId,
            Pageable pageable
    );
}
