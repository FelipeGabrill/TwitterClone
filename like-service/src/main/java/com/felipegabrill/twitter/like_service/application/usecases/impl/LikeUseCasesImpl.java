package com.felipegabrill.twitter.like_service.application.usecases.impl;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import com.felipegabrill.twitter.like_service.application.exceptions.LikeAlreadyExistsException;
import com.felipegabrill.twitter.like_service.application.exceptions.ResourceNotFoundException;
import com.felipegabrill.twitter.like_service.application.usecases.LikeUseCases;
import com.felipegabrill.twitter.like_service.application.publisher.ILikePublisher;
import com.felipegabrill.twitter.like_service.domain.like.Like;
import com.felipegabrill.twitter.like_service.domain.like.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class LikeUseCasesImpl implements LikeUseCases {

    private static final Logger log = LoggerFactory.getLogger(LikeUseCasesImpl.class);

    private final LikeRepository likeRepository;
    private final ILikePublisher likePublisher;

    public LikeUseCasesImpl(LikeRepository likeRepository, ILikePublisher likePublisher) {
        this.likeRepository = likeRepository;
        this.likePublisher = likePublisher;
    }

    @Transactional
    @Override
    public void like(UUID userId, UUID tweetId) {
        log.info("Creating like | userId={} | tweetId={}", userId, tweetId);

        try {
            Like like = createLike(userId, tweetId);
            likeRepository.save(like);

            log.info("Like persisted successfully | likeId={}", like.getId());

            likePublisher.publishLikeCreated(tweetId, userId);
            log.info("LikeCreated event published | tweetId={} | userId={}", tweetId, userId);

        } catch (DataIntegrityViolationException e) {
            log.warn("Like already exists | userId={} | tweetId={}", userId, tweetId);
            throw new LikeAlreadyExistsException(
                    "User " + userId + " already liked tweet " + tweetId
            );
        }
    }

    @Transactional
    @Override
    public void unlike(UUID userId, UUID tweetId) {
        log.info("Removing like | userId={} | tweetId={}", userId, tweetId);

        int deleted = likeRepository.deleteByTweetIdAndUserId(tweetId, userId);

        if (deleted == 0) {
            log.warn("Like not found | userId={} | tweetId={}", userId, tweetId);
            throw new ResourceNotFoundException(
                    "Like not found for user " + userId + " and tweet " + tweetId
            );
        }

        log.info("Like removed successfully | userId={} | tweetId={}", userId, tweetId);

        likePublisher.publishLikeDeleted(tweetId, userId);
        log.info("LikeDeleted event published | tweetId={} | userId={}", tweetId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public LikeStatusResponseDTO hasLiked(UUID userId, UUID tweetId) {
        log.debug("Checking if like exists | userId={} | tweetId={}", userId, tweetId);

        return new LikeStatusResponseDTO(
                likeRepository.existsByTweetIdAndUserId(tweetId, userId)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LikeResponseDTO> listLikesByTweetId(UUID tweetId, Pageable pageable) {
        log.debug("Fetching likes by tweet | tweetId={}", tweetId);

        return likeRepository.listLikesByTweetId(tweetId, pageable)
                .map(like -> new LikeResponseDTO(
                        like.getUserId(),
                        like.getCreatedAt()
                ));
    }

    private Like createLike(UUID userId, UUID tweetId) {
        Like like = new Like();
        like.setId(UUID.randomUUID());
        like.setUserId(userId);
        like.setTweetId(tweetId);
        like.setCreatedAt(Instant.now());
        return like;
    }
}
