package com.felipegabrill.twitter.tweet_service.database.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Media {

    private String mediaUrl;

    private int position;

    public Media() {
    }

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

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
