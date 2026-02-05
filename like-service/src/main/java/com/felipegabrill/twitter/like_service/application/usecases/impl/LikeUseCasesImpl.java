package com.felipegabrill.twitter.like_service.application.usecases.impl;

import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeResponseDTO;
import com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response.LikeStatusResponseDTO;
import com.felipegabrill.twitter.like_service.application.exceptions.LikeAlreadyExistsException;
import com.felipegabrill.twitter.like_service.application.exceptions.ResourceNotFoundException;
import com.felipegabrill.twitter.like_service.application.usecases.LikeUseCases;
import com.felipegabrill.twitter.like_service.domain.like.Like;
import com.felipegabrill.twitter.like_service.domain.like.repository.LikeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class LikeUseCasesImpl implements LikeUseCases {

    private final LikeRepository likeRepository;

    public LikeUseCasesImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Transactional
    @Override
    public void like(UUID userId, UUID tweetId) {
        try {
            Like like = createLike(userId, tweetId);
            likeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            throw new LikeAlreadyExistsException(
                    "User " + userId + " already liked tweet " + tweetId
            );
        }
    }

    @Transactional
    @Override
    public void unlike(UUID userId, UUID tweetId) {
        int deleted = likeRepository.deleteByTweetIdAndUserId(tweetId, userId);
        if (deleted == 0) {
            throw new ResourceNotFoundException(
                    "Like not found for user " + userId + " and tweet " + tweetId
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public LikeStatusResponseDTO hasLiked(UUID userId, UUID tweetId) {
        return new LikeStatusResponseDTO(likeRepository.existsByTweetIdAndUserId(tweetId, userId));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LikeResponseDTO> listLikesByTweetId(UUID tweetId, Pageable pageable) {

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
