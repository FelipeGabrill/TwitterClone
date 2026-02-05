package com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents whether a user has liked a specific tweet")
public class LikeStatusResponseDTO {

    @Schema(
            description = "Indicates if the user has liked the tweet",
            example = "true"
    )
    private final boolean liked;

    public LikeStatusResponseDTO(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }
}