package com.felipegabrill.twitter.like_service.adapters.inbound.dtos.response;

public class LikeStatusResponseDTO {

    private final boolean liked;

    public LikeStatusResponseDTO(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }
}