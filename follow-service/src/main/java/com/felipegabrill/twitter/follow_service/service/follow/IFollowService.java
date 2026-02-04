package com.felipegabrill.twitter.follow_service.service.follow;

import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IFollowService {

    FollowResponseDTO followUser(UUID followerId, UUID followingId);

    void unfollowUser(UUID followerId, UUID followingId);

    Page<FollowResponseDTO> getFollowing(UUID followerId, Pageable pageable);

    Page<FollowResponseDTO> getFollowers(UUID followingId, Pageable pageable);

    boolean isFollowing(UUID followerId, UUID followingId);

}
