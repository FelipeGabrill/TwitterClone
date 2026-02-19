package com.felipegabrill.twitter.feed_service.database.rds.entities;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_famous_users")
public class FamousUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private boolean isFamous;

    @Column(nullable = false)
    private Instant becameFamousAt;

    @Column(nullable = false)
    private Long followerCountAtPromotion;

    public FamousUserEntity() {
    }

    public FamousUserEntity(
            UUID userId,
            boolean isFamous,
            Instant becameFamousAt,
            Long followerCountAtPromotion
    ) {
        this.userId = userId;
        this.isFamous = isFamous;
        this.becameFamousAt = becameFamousAt;
        this.followerCountAtPromotion = followerCountAtPromotion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isFamous() {
        return isFamous;
    }

    public void setFamous(boolean famous) {
        isFamous = famous;
    }

    public Instant getBecameFamousAt() {
        return becameFamousAt;
    }

    public void setBecameFamousAt(Instant becameFamousAt) {
        this.becameFamousAt = becameFamousAt;
    }

    public Long getFollowerCountAtPromotion() {
        return followerCountAtPromotion;
    }

    public void setFollowerCountAtPromotion(Long followerCountAtPromotion) {
        this.followerCountAtPromotion = followerCountAtPromotion;
    }
}
