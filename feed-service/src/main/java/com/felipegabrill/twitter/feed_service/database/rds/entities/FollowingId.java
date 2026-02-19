package com.felipegabrill.twitter.feed_service.database.rds.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class FollowingId implements Serializable {

    private UUID userId;

    private UUID followedId;

    public FollowingId() {
    }

    public FollowingId(UUID userId, UUID followedId) {
        this.userId = userId;
        this.followedId = followedId;
    }

    public UUID getFollowedId() {
        return followedId;
    }

    public void setFollowedId(UUID followedId) {
        this.followedId = followedId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
