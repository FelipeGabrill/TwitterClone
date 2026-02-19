package com.felipegabrill.twitter.feed_service.database.rds.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "tb_follow")
public class FollowEntity {

    @EmbeddedId
    private FollowingId id;

    @Column(nullable = false)
    private Instant followedAt;

    public FollowEntity() {
    }

    public FollowEntity(FollowingId id, Instant followedAt) {
        this.id = id;
        this.followedAt = followedAt;
    }

    public FollowingId getId() {
        return id;
    }

    public void setId(FollowingId id) {
        this.id = id;
    }

    public Instant getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Instant followedAt) {
        this.followedAt = followedAt;
    }
}

