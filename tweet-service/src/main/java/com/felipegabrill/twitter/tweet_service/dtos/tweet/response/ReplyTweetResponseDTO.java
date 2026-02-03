package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.media.MediaDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReplyTweetResponseDTO extends BaseTweetResponseDTO {

    private final TweetType type = TweetType.REPLY;
    private final UUID replyToId;
    private final UUID rootTweetId;
    private final String content;
    private final List<MediaDTO> media;
    private final List<HashtagDTO> hashtags;
    private final List<UserMentionDTO> userMentions;
    private final Integer retweetCount;
    private final Integer replyCount;

    public ReplyTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            UUID replyToId,
            UUID rootTweetId,
            String content,
            List<MediaDTO> media,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions, Integer retweetCount, Integer replyCount
    ) {
        super(id, authorId, likeCount, createdAt);
        this.replyToId = replyToId;
        this.rootTweetId = rootTweetId;
        this.content = content;
        this.media = media;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
        this.retweetCount = retweetCount;
        this.replyCount = replyCount;
    }

    public TweetType getType() {
        return type;
    }

    public UUID getReplyToId() {
        return replyToId;
    }

    public UUID getRootTweetId() {
        return rootTweetId;
    }

    public String getContent() {
        return content;
    }

    public List<MediaDTO> getMedia() {
        return media;
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
