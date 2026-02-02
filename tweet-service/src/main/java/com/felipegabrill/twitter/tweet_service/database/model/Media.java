package com.felipegabrill.twitter.tweet_service.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Media {

    @Column(name = "media_url", nullable = false, length = 500)
    private final String mediaUrl;

    @Column(name = "position", nullable = false)
    private final int position;

    public Media(String mediaUrl, int position) {
        this.mediaUrl = mediaUrl;
        this.position = position;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public int getPosition() {
        return position;
    }
}
