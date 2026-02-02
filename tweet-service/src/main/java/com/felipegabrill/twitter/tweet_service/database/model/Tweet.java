package com.felipegabrill.twitter.tweet_service.database.model;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_tweet")
public class Tweet {

    @Id
    private UUID id;

    private UUID authorId;

    @Column(length = 280)
    private String content;

    private UUID replyToId;

    private UUID retweetOfId;

    private UUID rootTweetId;

    @ElementCollection
    @CollectionTable(
            name = "tb_tweet_media",
            joinColumns = @JoinColumn(name = "tweet_id")
    )
    @OrderBy("position ASC")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Media> media = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "tb_tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Hashtag> hashtags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "tb_tweet_mentions",
            joinColumns = @JoinColumn(name = "tweet_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<UserMention> userMentions = new ArrayList<>();

    private Integer likeCount;

    private Integer retweetCount;

    private Integer replyCount;

    @Column(updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private TweetType type;

    private Boolean deleted = false;

    @Column(updatable = false)
    private Instant deletedAt;

    public Tweet() {
    }

    public Tweet(UUID id, UUID authorId, String content, UUID replyToId, UUID retweetOfId, UUID rootTweetId, List<Media> media, Integer likeCount, Integer retweetCount, Instant createdAt, TweetType type, Integer replyCount, Boolean deleted, Instant deletedAt) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.replyToId = replyToId;
        this.retweetOfId = retweetOfId;
        this.rootTweetId = rootTweetId;
        this.media = media;
        this.likeCount = likeCount;
        this.retweetCount = retweetCount;
        this.createdAt = createdAt;
        this.type = type;
        this.replyCount = replyCount;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(UUID replyToId) {
        this.replyToId = replyToId;
    }

    public UUID getRetweetOfId() {
        return retweetOfId;
    }

    public void setRetweetOfId(UUID retweetOfId) {
        this.retweetOfId = retweetOfId;
    }

    public UUID getRootTweetId() {
        return rootTweetId;
    }

    public void setRootTweetId(UUID rootTweetId) {
        this.rootTweetId = rootTweetId;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public TweetType getType() {
        return type;
    }

    public void setType(TweetType type) {
        this.type = type;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }
}
