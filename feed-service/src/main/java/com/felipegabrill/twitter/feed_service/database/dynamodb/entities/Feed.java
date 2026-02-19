package com.felipegabrill.twitter.feed_service.database.dynamodb.entities;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.time.Instant;

@DynamoDbBean
public class Feed {

    private String pk;
    private String sk;

    private String tweetId;
    private String authorId;
    private Instant createdAt;
    private String feedType;

    private String gsiDatePk;
    private String gsiDateSk;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    @DynamoDbAttribute("tweetId")
    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    @DynamoDbAttribute("authorId")
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @DynamoDbAttribute("createdAt")
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDbAttribute("feedType")
    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "GSI_DATE")
    @DynamoDbAttribute("GSI_DATE_PK")
    public String getGsiDatePk() {
        return gsiDatePk;
    }

    public void setGsiDatePk(String gsiDatePk) {
        this.gsiDatePk = gsiDatePk;
    }

    @DynamoDbSecondarySortKey(indexNames = "GSI_DATE")
    @DynamoDbAttribute("GSI_DATE_SK")
    public String getGsiDateSk() {
        return gsiDateSk;
    }

    public void setGsiDateSk(String gsiDateSk) {
        this.gsiDateSk = gsiDateSk;
    }

}
