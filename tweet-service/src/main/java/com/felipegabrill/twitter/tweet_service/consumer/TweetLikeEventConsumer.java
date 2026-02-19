package com.felipegabrill.twitter.tweet_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.consumer.events.TweetLikeEvent;
import com.felipegabrill.twitter.tweet_service.consumer.exceptions.NonRetryableEventException;
import com.felipegabrill.twitter.tweet_service.service.tweet.ITweetService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TweetLikeEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(TweetLikeEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ITweetService tweetService;

    public TweetLikeEventConsumer(
            ObjectMapper objectMapper,
            ITweetService tweetService
    ) {
        this.objectMapper = objectMapper;
        this.tweetService = tweetService;
    }

    @SqsListener("${aws.sqs.like-events-queue-url}")
    public void consume(String message) {

        try {
            TweetLikeEvent event = parseEvent(message);

            log.info(
                    "Processing TweetLikeEvent | eventType={} | tweetId={}",
                    event.eventType(),
                    event.data().tweetId()
            );

            handleEvent(event);

        } catch (NonRetryableEventException ex) {
            log.error("Non-retryable error while processing message. Discarding. body={}", message, ex);
        } catch (Exception ex) {
            log.error("Retryable error while processing message. Will retry.", ex);
            throw ex;
        }
    }

    private TweetLikeEvent parseEvent(String message) {

        try {
            JsonNode snsEnvelope = objectMapper.readTree(message);
            String payload = snsEnvelope.path("Message").asText("");

            if (payload.isBlank()) {
                throw new NonRetryableEventException(
                        "SNS envelope missing or empty 'Message'"
                );
            }

            TweetLikeEvent event =
                    objectMapper.readValue(payload, TweetLikeEvent.class);

            validate(event);

            return event;

        } catch (JsonProcessingException e) {
            throw new NonRetryableEventException(
                    "Failed to deserialize TweetLikeEvent"
            );
        }
    }

    private void validate(TweetLikeEvent event) {

        if (event.eventType() == null ||
                event.data() == null ||
                event.data().tweetId() == null) {

            throw new NonRetryableEventException(
                    "Missing required fields in TweetLikeEvent"
            );
        }
    }

    private void handleEvent(TweetLikeEvent event) {

        switch (event.eventType()) {
            case "LikeCreated" ->
                    tweetService.likeTweet(event.data().tweetId());

            case "LikeDeleted" ->
                    tweetService.unlikeTweet(event.data().tweetId());

            default ->
                    log.warn("Unknown eventType received: {}", event.eventType());
        }
    }
}
