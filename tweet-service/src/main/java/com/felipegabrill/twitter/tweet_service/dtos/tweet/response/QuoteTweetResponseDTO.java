package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class QuoteTweetResponseDTO extends BaseTweetResponseDTO {

    private final TweetType type = TweetType.QUOTE;
    private final UUID retweetOfId;
    private final String content;
    private final List<String> mediaUrls;
    private final List<HashtagDTO> hashtags;
    private final List<UserMentionDTO> userMentions;
    private final Integer retweetCount;
    private final Integer replyCount;

    public QuoteTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            UUID retweetOfId,
            String content,
            List<String> mediaUrls,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions, Integer retweetCount, Integer replyCount
    ) {
        super(id, authorId, likeCount, createdAt);
        this.retweetOfId = retweetOfId;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
        this.retweetCount = retweetCount;
        this.replyCount = replyCount;
    }

    public TweetType getType() {
        return type;
    }

    public UUID getRetweetOfId() {
        return retweetOfId;
    }

    public String getContent() {
        return content;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public List<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public List<UserMentionDTO> getUserMentions() {
        return userMentions;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }
}
