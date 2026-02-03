package com.felipegabrill.twitter.tweet_service.dtos.media;

public class MediaDTO {

    private String mediaUrl;

    private int position;

    public MediaDTO() {
    }

    public MediaDTO(String mediaUrl, int position) {
        this.mediaUrl = mediaUrl;
        this.position = position;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
