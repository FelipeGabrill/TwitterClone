package com.felipegabrill.twitter.follow_service.service.follow.impl;

import com.felipegabrill.twitter.follow_service.database.model.Follow;
import com.felipegabrill.twitter.follow_service.database.repository.FollowRepository;
import com.felipegabrill.twitter.follow_service.dtos.follow.FollowResponseDTO;
import com.felipegabrill.twitter.follow_service.mapper.FollowMapper;
import com.felipegabrill.twitter.follow_service.publisher.IFollowPublisher;
import com.felipegabrill.twitter.follow_service.service.follow.IFollowService;
import com.felipegabrill.twitter.follow_service.service.exceptions.AlreadyFollowingException;
import com.felipegabrill.twitter.follow_service.service.exceptions.NotFollowingException;
import com.felipegabrill.twitter.follow_service.service.exceptions.UserCannotFollowSelfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class FollowServiceImpl implements IFollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;
    private final IFollowPublisher followPublisher;

    @Autowired
    public FollowServiceImpl(
            FollowRepository followRepository,
            FollowMapper followMapper,
            IFollowPublisher followPublisher
    ) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
        this.followPublisher = followPublisher;
    }

    @Override
    @Transactional
    public FollowResponseDTO followUser(UUID followerId, UUID followingId) {
        logger.info(
                "Request to follow user. followerId={}, followingId={}",
                followerId, followingId
        );

        validateFollow(followerId, followingId);

        Follow follow = buildFollow(followerId, followingId);
        Follow savedFollow = followRepository.save(follow);

        logger.info(
                "User followed successfully. followerId={}, followingId={}, followId={}",
                followerId, followingId, savedFollow.getId()
        );

        followPublisher.publishUserFollowed(followerId, followingId);

        logger.debug(
                "Follow event published to SNS. followerId={}, followingId={}",
                followerId, followingId
        );

        return followMapper.toDTO(savedFollow);
    }

    @Override
    @Transactional
    public void unfollowUser(UUID followerId, UUID followingId) {
        logger.info(
                "Request to unfollow user. followerId={}, followingId={}",
                followerId, followingId
        );

        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            logger.warn(
                    "Unfollow failed: user is not following target. followerId={}, followingId={}",
                    followerId, followingId
            );
            throw new NotFollowingException("User is not following the target");
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);

        logger.info(
                "User unfollowed successfully. followerId={}, followingId={}",
                followerId, followingId
        );

        followPublisher.publishUserUnfollowed(followerId, followingId);

        logger.debug(
                "Unfollow event published to SNS. followerId={}, followingId={}",
                followerId, followingId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowing(UUID followerId, Pageable pageable) {
        logger.debug(
                "Fetching following list. followerId={}, page={}, size={}",
                followerId, pageable.getPageNumber(), pageable.getPageSize()
        );

        return followRepository.findAllByFollowerId(followerId, pageable)
                .map(followMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponseDTO> getFollowers(UUID followingId, Pageable pageable) {
        logger.debug(
                "Fetching followers list. followingId={}, page={}, size={}",
                followingId, pageable.getPageNumber(), pageable.getPageSize()
        );

        return followRepository.findAllByFollowingId(followingId, pageable)
                .map(followMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(UUID followerId, UUID followingId) {
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);

        logger.debug(
                "Checking follow relationship. followerId={}, followingId={}, isFollowing={}",
                followerId, followingId, isFollowing
        );

        return isFollowing;
    }

    private Follow buildFollow(UUID followerId, UUID followingId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedAt(Instant.now());

        logger.debug(
                "Building Follow entity. followerId={}, followingId={}",
                followerId, followingId
        );

        return follow;
    }

    private void validateFollow(UUID followerId, UUID followingId) {
        if (followerId.equals(followingId)) {
            logger.warn(
                    "Invalid follow attempt: user tried to follow themselves. userId={}",
                    followerId
            );
            throw new UserCannotFollowSelfException("User cannot follow themselves");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            logger.warn(
                    "Invalid follow attempt: already following. followerId={}, followingId={}",
                    followerId, followingId
            );
            throw new AlreadyFollowingException("User is already following the target");
        }
    }
}
