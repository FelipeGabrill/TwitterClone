package com.felipegabrill.twitter.tweet_service.database.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Hashtag {
    private String text;
    private Integer startIndex;
    private Integer endIndex;

    public Hashtag() {}
    public Hashtag(String text, Integer startIndex, Integer endIndex) {
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