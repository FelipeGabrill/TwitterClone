package com.felipegabrill.twitter.tweet_service.dtos.media;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "MediaDTO",
        description = "Represents a media item attached to a tweet, including its URL and display position."
)
public class MediaDTO {

    @Schema(
            description = "Public URL of the media file",
            example = "https://cdn.twitterclone.com/media/abc123.jpg"
    )
    private String mediaUrl;

    @Schema(
            description = "Position of the media in the tweet (used for ordering)",
            example = "0",
            minimum = "0"
    )
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
