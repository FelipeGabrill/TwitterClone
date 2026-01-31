package com.felipegabrill.twitter.user_service.domain.user.projections;

import java.util.UUID;

public interface UserPreviewProjection {

    UUID getId();
    String getUsername();
    String getProfileImageUrl();

}
