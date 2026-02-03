package com.felipegabrill.twitter.tweet_service.dtos.hashtag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class HashtagDTO {

    @NotBlank(message = "Hashtag text must not be blank")
    @Size(min = 1, max = 100, message = "Hashtag text must be between 1 and 100 characters")
    private String text;

    @NotNull(message = "startIndex is required")
    @PositiveOrZero(message = "startIndex must be zero or positive")
    private Integer startIndex;

    @NotNull(message = "endIndex is required")
    @PositiveOrZero(message = "endIndex must be zero or positive")
    private Integer endIndex;

    public HashtagDTO() {}
    public HashtagDTO(String text, Integer startIndex, Integer endIndex) {
        this.text = text;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
}