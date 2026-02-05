package com.felipegabrill.twitter.like_service.domain.like.projections;

import java.time.Instant;
import java.util.UUID;

public interface LikePreviewProjection {


    UUID getUserId();
    Instant getCreatedAt();
}
