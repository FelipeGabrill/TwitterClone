package com.felipegabrill.twitter.feed_service.database.rds.repositories;

import com.felipegabrill.twitter.feed_service.database.rds.entities.FollowEntity;
import com.felipegabrill.twitter.feed_service.database.rds.entities.FollowingId;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowingId> {

    @Query("""
    SELECT f.id.followedId
    FROM FollowEntity f
    WHERE f.id.userId = :userId
    """)
    List<UUID> findFollowedIdsByUserId(@Param("userId") UUID userId);


}

