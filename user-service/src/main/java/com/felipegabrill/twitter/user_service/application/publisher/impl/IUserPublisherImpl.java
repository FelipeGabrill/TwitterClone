package com.felipegabrill.twitter.user_service.application.publisher.impl;

import com.felipegabrill.twitter.user_service.application.publisher.IUserPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;
import java.util.UUID;

@Service
public class IUserPublisherImpl implements IUserPublisher {

    private static final Logger logger = LoggerFactory.getLogger(IUserPublisherImpl.class);

    private final SnsAsyncClient snsClient;

    @Value("${aws.sns.high-followers-topic-arn}")
    private String highFollowersTopicArn;

    public IUserPublisherImpl(SnsAsyncClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void sendHighFollowersEvent(UUID userId, long followersCount) {
        String eventId = UUID.randomUUID().toString();
        String eventType = "UserReachedHighFollowers";

        logger.info(
                "Publishing SNS high followers event. eventType={}, eventId={}, userId={}, followersCount={}",
                eventType, eventId, userId, followersCount
        );

        String payload = """
                {
                  "eventId": "%s",
                  "eventType": "%s",
                  "data": {
                    "userId": "%s",
                    "followersCount": %d,
                    "occurredAt": "%s"
                  }
                }
                """.formatted(
                eventId,
                eventType,
                userId,
                followersCount,
                Instant.now()
        );

        snsClient.publish(
                        PublishRequest.builder()
                                .topicArn(highFollowersTopicArn)
                                .message(payload)
                                .build()
                )
                .thenAccept(response ->
                        logger.info(
                                "SNS high followers event published successfully. eventId={}, messageId={}",
                                eventId, response.messageId()
                        )
                )
                .exceptionally(ex -> {
                    logger.error(
                            "Failed to publish SNS high followers event. eventId={}, userId={}",
                            eventId, userId, ex
                    );
                    return null;
                });
    }
}