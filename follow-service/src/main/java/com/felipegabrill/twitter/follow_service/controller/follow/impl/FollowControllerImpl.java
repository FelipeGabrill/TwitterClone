package com.felipegabrill.twitter.follow_service.controller.follow.impl;

import com.felipegabrill.twitter.follow_service.controller.follow.IFollowController;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import com.felipegabrill.twitter.follow_service.service.follow.IFollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FollowControllerImpl implements IFollowController {

    private static final Logger logger = LoggerFactory.getLogger(FollowControllerImpl.class);

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
        logger.info(
                "HTTP POST /follow requested. followerId={}, followingId={}",
                followerId, followingId
        );

        FollowResponseDTO response = followService.followUser(followerId, followingId);

        logger.info(
                "User followed successfully. followerId={}, followingId={}",
                followerId, followingId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @DeleteMapping("/users/{followerId}/unfollow/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    ) {
        logger.info(
                "HTTP DELETE /unfollow requested. followerId={}, followingId={}",
                followerId, followingId
        );

        followService.unfollowUser(followerId, followingId);

        logger.info(
                "User unfollowed successfully. followerId={}, followingId={}",
                followerId, followingId
        );

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/users/{followerId}/following")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowing(
            @PathVariable UUID followerId,
            Pageable pageable
    ) {
        logger.info(
                "HTTP GET /following requested. followerId={}, page={}, size={}",
                followerId, pageable.getPageNumber(), pageable.getPageSize()
        );

        Page<FollowResponseDTO> response = followService.getFollowing(followerId, pageable);

        logger.info(
                "Following list retrieved. followerId={}, totalElements={}",
                followerId, response.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/users/{followingId}/followers")
    public ResponseEntity<Page<FollowResponseDTO>> getFollowers(
            @PathVariable UUID followingId,
            Pageable pageable
    ) {
        logger.info(
                "HTTP GET /followers requested. followingId={}, page={}, size={}",
                followingId, pageable.getPageNumber(), pageable.getPageSize()
        );

        Page<FollowResponseDTO> response = followService.getFollowers(followingId, pageable);

        logger.info(
                "Followers list retrieved. followingId={}, totalElements={}",
                followingId, response.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/users/{followerId}/is-following/{followingId}")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID followerId,
            @PathVariable UUID followingId
    ) {
        logger.debug(
                "HTTP GET /is-following requested. followerId={}, followingId={}",
                followerId, followingId
        );

        boolean response = followService.isFollowing(followerId, followingId);

        logger.debug(
                "Is-following result. followerId={}, followingId={}, result={}",
                followerId, followingId, response
        );

        return ResponseEntity.ok(response);
    }
}
