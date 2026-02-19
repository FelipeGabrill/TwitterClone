package com.felipegabrill.twitter.feed_service.services.impl;

import com.felipegabrill.twitter.feed_service.database.rds.entities.FollowEntity;
import com.felipegabrill.twitter.feed_service.database.rds.entities.FollowingId;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.FollowRepository;
import com.felipegabrill.twitter.feed_service.services.IFollowService;
import com.felipegabrill.twitter.feed_service.services.exceptions.FollowPersistenceException;
import com.felipegabrill.twitter.feed_service.services.exceptions.InvalidFollowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FollowServiceImpl implements IFollowService {

    private static final Logger log =
            LoggerFactory.getLogger(FollowServiceImpl.class);

    private final FollowRepository followRepository;

    public FollowServiceImpl(
            FollowRepository followRepository
    ) {
        this.followRepository = followRepository;
    }

    @Override
    @Transactional
    public void followUser(UUID followerId, UUID followedId) {

        validate(followerId, followedId);

        try {

            FollowingId id = new FollowingId(followerId, followedId);

            if (followRepository.existsById(id)) {
                log.warn(
                        "Follow already exists | followerId={} | followedId={}",
                        followerId,
                        followedId
                );
                return;
            }

            FollowEntity entity = new FollowEntity(id, Instant.now());

            followRepository.save(entity);

            log.info(
                    "Follow created | followerId={} | followedId={}",
                    followerId,
                    followedId
            );

        } catch (Exception ex) {

            log.error(
                    "Error persisting follow | followerId={} | followedId={}",
                    followerId,
                    followedId,
                    ex
            );

            throw new FollowPersistenceException(
                    "Failed to persist follow relationship"
            );
        }
    }

    @Override
    @Transactional
    public void unfollowUser(UUID followerId, UUID followedId) {

        validate(followerId, followedId);

        try {

            FollowingId id = new FollowingId(followerId, followedId);

            followRepository.deleteById(id);

            log.info(
                    "Follow removed | followerId={} | followedId={}",
                    followerId,
                    followedId
            );

        } catch (Exception ex) {

            log.error(
                    "Error removing follow | followerId={} | followedId={}",
                    followerId,
                    followedId,
                    ex
            );

            throw new FollowPersistenceException(
                    "Failed to remove follow relationship"
            );
        }
    }

    @Override
    public List<String> getFollowedUsers(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        return followRepository.findFollowedIdsByUserId(userId)
                .stream()
                .map(UUID::toString)
                .toList();
    }


    private void validate(UUID followerId, UUID followedId) {

        if (followerId == null || followedId == null) {
            throw new InvalidFollowException(
                    "FollowerId and FollowedId must not be null"
            );
        }

        if (followerId.equals(followedId)) {
            throw new InvalidFollowException(
                    "User cannot follow themselves"
            );
        }
    }
}
