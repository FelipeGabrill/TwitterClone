package com.felipegabrill.twitter.tweet_service.database.repository;

import com.felipegabrill.twitter.tweet_service.database.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Tweet t SET t.likeCount = t.likeCount + 1 WHERE t.id = :tweetId")
    void incrementLikeCount(@Param("tweetId") UUID tweetId);

    @Modifying
    @Transactional
    @Query("UPDATE Tweet t SET t.likeCount = CASE WHEN t.likeCount > 0 THEN t.likeCount - 1 ELSE 0 END WHERE t.id = :tweetId")
    void decrementLikeCount(@Param("tweetId") UUID tweetId);

}
