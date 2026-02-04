package com.felipegabrill.twitter.follow_service.controller.follow.impl;

import com.felipegabrill.twitter.follow_service.controller.follow.IFollowController;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import com.felipegabrill.twitter.follow_service.service.follow.IFollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FollowControllerImpl implements IFollowController {

    private final IFollowService followService;

    public FollowControllerImpl(IFollowService followService) {
        this.followService = followService;
    }

    @Override
    @PostMapping("/users/{followerId}/follow/{followingId}")
    public ResponseEntity<FollowResponseDTO> followUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    ) {
        FollowResponseDTO response = followService.followUser(followerId, followingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @DeleteMapping("/users/{followerId}/unfollow/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    ) {
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/users/{followerId}/following")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowing(
            @PathVariable UUID followerId,
            Pageable pageable
    ) {
        Page<FollowResponseDTO> response = followService.getFollowing(followerId, pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/users/{followingId}/followers")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowers(
            @PathVariable UUID followingId,
            Pageable pageable
    ) {
        Page<FollowResponseDTO> response = followService.getFollowers(followingId, pageable);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/users/{followerId}/is-following/{followingId}")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    ) {
        boolean response = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(response);
    }
}
