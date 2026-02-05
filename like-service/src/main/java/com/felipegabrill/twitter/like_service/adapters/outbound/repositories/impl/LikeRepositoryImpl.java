package com.felipegabrill.twitter.like_service.adapters.outbound.repositories.impl;

import com.felipegabrill.twitter.like_service.adapters.outbound.entities.JpaLikeEntity;
import com.felipegabrill.twitter.like_service.adapters.outbound.repositories.JpaLikeRepository;
import com.felipegabrill.twitter.like_service.domain.like.Like;
import com.felipegabrill.twitter.like_service.domain.like.projections.LikePreviewProjection;
import com.felipegabrill.twitter.like_service.domain.like.repository.LikeRepository;
import com.felipegabrill.twitter.like_service.infrastructure.mappers.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final JpaLikeRepository jpaLikeRepository;
    private final LikeMapper likeMapper;

    @Autowired
    public LikeRepositoryImpl(JpaLikeRepository jpaLikeRepository,
                              LikeMapper likeMapper) {
        this.jpaLikeRepository = jpaLikeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public Like save(Like like) {
        JpaLikeEntity entity = likeMapper.toEntity(like);
        JpaLikeEntity saved = jpaLikeRepository.saveAndFlush(entity);
        return likeMapper.toDomain(saved);
    }

    @Override
    public boolean existsByTweetIdAndUserId(UUID tweetId, UUID userId) {
        return jpaLikeRepository.existsByTweetIdAndUserId(tweetId, userId);
    }

    @Override
    public Optional<Like> findByTweetIdAndUserId(UUID tweetId, UUID userId) {
        return jpaLikeRepository
                .findByTweetIdAndUserId(tweetId, userId)
                .map(likeMapper::toDomain);
    }

    @Override
    public int deleteByTweetIdAndUserId(UUID tweetId, UUID userId) {
        return jpaLikeRepository.deleteByTweetIdAndUserId(tweetId, userId);
    }

    @Override
    public Page<LikePreviewProjection> listLikesByTweetId(UUID tweetId, Pageable pageable) {
        return jpaLikeRepository.findAllByTweetId(tweetId, pageable);
    }
}
