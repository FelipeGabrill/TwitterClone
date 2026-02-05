package com.felipegabrill.twitter.like_service.domain.like.repository;

import com.felipegabrill.twitter.like_service.domain.like.Like;
import com.felipegabrill.twitter.like_service.domain.like.projections.LikePreviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository {

    Like save(Like like);

    boolean existsByTweetIdAndUserId(UUID tweetId, UUID userId);

    Optional<Like> findByTweetIdAndUserId(UUID tweetId, UUID userId);

    int deleteByTweetIdAndUserId(UUID tweetId, UUID userId);

    Page<LikePreviewProjection> listLikesByTweetId(UUID tweetId, Pageable pageable);

}
