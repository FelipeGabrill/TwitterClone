package com.felipegabrill.twitter.follow_service.controller.follow;

import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface IFollowController {

    @PostMapping("/users/{followerId}/follow/{followingId}")
    ResponseEntity<FollowResponseDTO> followUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );

    @DeleteMapping("/users/{followerId}/unfollow/{followingId}")
    ResponseEntity<Void> unfollowUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );

    @GetMapping("/users/{followerId}/following")
    ResponseEntity<Page<FollowResponseDTO>> getFollowing(
            @PathVariable UUID followerId,
            Pageable pageable
    );

    @GetMapping("/users/{followingId}/followers")
    ResponseEntity<Page<FollowResponseDTO>> getFollowers(
            @PathVariable UUID followingId,
            Pageable pageable
    );

    @GetMapping("/users/{followerId}/is-following/{followingId}")
    ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    );
}
