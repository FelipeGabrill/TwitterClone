package com.felipegabrill.twitter.tweet_service.dtos.tweet.response;

import com.felipegabrill.twitter.tweet_service.database.model.enums.TweetType;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.media.MediaDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response DTO for a normal tweet (original post)")
public class NormalTweetResponseDTO extends BaseTweetResponseDTO {

    @Schema(
            description = "Type of the tweet",
            example = "NORMAL"
    )
    private final TweetType type = TweetType.NORMAL;

    @Schema(
            description = "Text content of the tweet",
            example = "Hello world! #java"
    )
    private final String content;

    @Schema(
            description = "Media attached to the tweet (images, videos, etc). Can be empty or null"
    )
    private final List<MediaDTO> media;

    @Schema(
            description = "Hashtags extracted from the tweet content"
    )
    private final List<HashtagDTO> hashtags;

    @Schema(
            description = "Users mentioned in the tweet content"
    )
    private final List<UserMentionDTO> userMentions;

    @Schema(
            description = "Total number of retweets for this tweet",
            example = "12"
    )
    private final Integer retweetCount;

    @Schema(
            description = "Total number of replies to this tweet",
            example = "3"
    )
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
