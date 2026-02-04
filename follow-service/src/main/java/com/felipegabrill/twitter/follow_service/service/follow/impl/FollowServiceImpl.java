package com.felipegabrill.twitter.follow_service.service.follow.impl;

import com.felipegabrill.twitter.follow_service.database.model.Follow;
import com.felipegabrill.twitter.follow_service.database.repository.FollowRepository;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import com.felipegabrill.twitter.follow_service.mapper.FollowMapper;
import com.felipegabrill.twitter.follow_service.service.follow.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class FollowServiceImpl implements IFollowService {

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
    }

    @Override
    @Transactional
    public FollowResponseDTO followUser(UUID followerId, UUID followingId) {
        validateFollow(followerId, followingId);

        Follow savedFollow = followRepository.save(buildFollow(followerId, followingId));
        return followMapper.toDTO(savedFollow);
    }

    @Override
    @Transactional
    public void unfollowUser(UUID followerId, UUID followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("User is not following the target.");
        }
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowing(UUID followerId, Pageable pageable) {
        return followRepository.findAllByFollowerId(followerId, pageable)
                .map(followMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowers(UUID followingId, Pageable pageable) {
        return followRepository.findAllByFollowingId(followingId, pageable)
                .map(followMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(UUID followerId, UUID followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    private Follow buildFollow(UUID followerId, UUID followingId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedAt(Instant.now());
        return follow;
    }

    private void validateFollow(UUID followerId, UUID followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("User is already following the target.");
        }
    }
}
