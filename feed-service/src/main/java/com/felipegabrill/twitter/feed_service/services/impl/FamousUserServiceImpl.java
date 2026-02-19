package com.felipegabrill.twitter.feed_service.services.impl;

import com.felipegabrill.twitter.feed_service.database.rds.entities.FamousUserEntity;
import com.felipegabrill.twitter.feed_service.database.rds.repositories.FamousUserRepository;
import com.felipegabrill.twitter.feed_service.services.IFamousUserService;
import com.felipegabrill.twitter.feed_service.services.exceptions.FollowPersistenceException;
import com.felipegabrill.twitter.feed_service.services.exceptions.InvalidFollowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class FamousUserServiceImpl implements IFamousUserService {

    private static final Logger log =
            LoggerFactory.getLogger(FamousUserServiceImpl.class);

    private final FamousUserRepository famousUserRepository;

    public FamousUserServiceImpl(FamousUserRepository famousUserRepository) {
        this.famousUserRepository = famousUserRepository;
    }

    @Override
    @Transactional
    public void promoteToFamous(UUID userId, long followerCount) {
        if (userId == null) {
            throw new InvalidFollowException("UserId must not be null");
        }

        try {
            FamousUserEntity newFamousUser = new FamousUserEntity(
                    userId,
                    true,
                    Instant.now(),
                    followerCount
            );

            famousUserRepository.save(newFamousUser);

            log.info("FamousUserEntity created and promoted | userId={} | followers={}", userId, followerCount);

        } catch (Exception ex) {
            log.error("Error creating famous user | userId={}", userId, ex);
            throw new FollowPersistenceException("Failed to create famous user");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public boolean isUserFamous(UUID userId) {

        if (userId == null) {
            throw new InvalidFollowException("UserId must not be null");
        }

        return famousUserRepository.existsByUserIdAndIsFamousTrue(userId);
    }
}
