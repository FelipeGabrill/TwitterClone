package com.felipegabrill.twitter.tweet_service.dtos.tweet;

import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class ReplyTweetDTO implements TweetWithEntities {

    private UUID replyToTweetId;
    private String content;
    private List<MultipartFile> media;
    private List<HashtagDTO> hashtags;
    private List<UserMentionDTO> userMentions;

    public ReplyTweetDTO() {
    }

    public ReplyTweetDTO(UUID replyToTweetId, String content, List<MultipartFile> media,
                         List<HashtagDTO> hashtags, List<UserMentionDTO> userMentions) {
        this.replyToTweetId = replyToTweetId;
        this.content = content;
        this.media = media;
        this.hashtags = hashtags;
        this.userMentions = userMentions;
    }

    public UUID getReplyToTweetId() {
        return replyToTweetId;
    }

    public void setReplyToTweetId(UUID replyToTweetId) {
        this.replyToTweetId = replyToTweetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MultipartFile> getMedia() {
        return media;
    }

    public void setMedia(List<MultipartFile> media) {
        this.media = media;
    }

    @Override
    public List<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<HashtagDTO> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public List<UserMentionDTO> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMentionDTO> userMentions) {
        this.userMentions = userMentions;
    }
}
