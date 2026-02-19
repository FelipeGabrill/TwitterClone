package com.felipegabrill.twitter.feed_service.database.rds.repositories;

import com.felipegabrill.twitter.feed_service.database.rds.entities.TweetEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TweetRepository extends JpaRepository<TweetEntity, UUID> {

    @Query("""
    SELECT t
    FROM TweetEntity t
    WHERE t.authorId IN (
        SELECT f.id.followedId
        FROM FollowEntity f
        JOIN FamousUserEntity fu ON fu.userId = f.id.followedId
        WHERE f.id.userId = :userId
          AND fu.isFamous = true
    )
    AND (:lastCreatedAt IS NULL OR 
        t.createdAt < :lastCreatedAt OR 
        (t.createdAt = :lastCreatedAt AND t.tweetId < :lastTweetId)
    )
    ORDER BY t.createdAt DESC, t.tweetId DESC
""")
    List<TweetEntity> findRecentFamousTweets(
            @Param("userId") UUID userId,
            @Param("lastCreatedAt") Instant lastCreatedAt,
            @Param("lastTweetId") UUID lastTweetId,
            Pageable pageable
    );


}
