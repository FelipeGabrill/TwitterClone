package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.media.MediaDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class NormalTweetResponseDTO extends BaseTweetResponseDTO {

    private final TweetType type = TweetType.NORMAL;
    private final String content;
    private final List<MediaDTO> media;
    private final List<HashtagDTO> hashtags;
    private final List<UserMentionDTO> userMentions;     // nova
    private final Integer retweetCount;
    private final Integer replyCount;

    public NormalTweetResponseDTO(
            UUID id,
            UUID authorId,
            Integer likeCount,
            Instant createdAt,
            String content,
            List<MediaDTO> media,
            List<HashtagDTO> hashtags,
            List<UserMentionDTO> userMentions,
            Integer retweetCount,
            Integer replyCount
    ) {
        super(id, authorId, likeCount, createdAt);
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
