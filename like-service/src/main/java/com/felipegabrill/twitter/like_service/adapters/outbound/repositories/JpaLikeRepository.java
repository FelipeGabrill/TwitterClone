package com.felipegabrill.twitter.like_service.adapters.outbound.repositories;

import com.felipegabrill.twitter.like_service.adapters.outbound.entities.JpaLikeEntity;
import com.felipegabrill.twitter.like_service.domain.like.projections.LikePreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaLikeRepository extends JpaRepository<JpaLikeEntity, UUID> {

    boolean existsByTweetIdAndUserId(UUID tweetId, UUID userId);

    Optional<JpaLikeEntity> findByTweetIdAndUserId(UUID tweetId, UUID userId);

    int deleteByTweetIdAndUserId(UUID tweetId, UUID userId);

    Page<LikePreviewProjection> findAllByTweetId(UUID tweetId, Pageable pageable);

}
