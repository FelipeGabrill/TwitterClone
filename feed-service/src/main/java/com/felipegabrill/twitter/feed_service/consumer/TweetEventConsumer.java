package com.felipegabrill.twitter.feed_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.feed_service.consumer.events.tweet.TweetEvent;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.NonRetryableEventException;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.RetryableEventException;
import com.felipegabrill.twitter.feed_service.services.IFeedService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TweetEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(TweetEventConsumer.class);

    private final IFeedService feedService;
    private final ObjectMapper objectMapper;

    public TweetEventConsumer(
            IFeedService feedService, ObjectMapper objectMapper
    ) {
        this.feedService = feedService;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${aws.sqs.tweet-events-queue-url}")
    public void consume(String message) {

        try {
            TweetEvent tweetData = parseEvent(message);

            log.info(
                    "Processing TweetEvent | tweetId={} | authorId={} | tweetType={} | rootTweetId={}",
                    tweetData.data().tweetId(),
                    tweetData.data().authorId(),
                    tweetData.data().tweetType(),
                    tweetData.data().rootTweetId()
            );

            handleEvent(tweetData);

        } catch (NonRetryableEventException ex) {
            log.error(
                    "Non-retryable error while processing TweetEvent. Discarding. body={}",
                    message,
                    ex
            );
        } catch (Exception ex) {
            log.error(
                    "Retryable error while processing TweetEvent. Will retry.",
                    ex
            );
            throw ex;
        }
    }

    private TweetEvent parseEvent(String message) {

        try {
            JsonNode envelope = objectMapper.readTree(message);
            String payload = envelope.path("Message").asText("");

            if (payload.isBlank()) {
                throw new NonRetryableEventException(
                        "SNS envelope missing or empty 'Message'"
                );
            }

            TweetEvent event =
                    objectMapper.readValue(payload, TweetEvent.class);

            validate(event);

            return event;

        } catch (JsonProcessingException e) {
            throw new NonRetryableEventException(
                    "Failed to deserialize TweetEvent"
            );
        }
    }

    private void validate(TweetEvent event) {

        if (event.data().tweetId() == null || event.data().authorId() == null) {
            throw new NonRetryableEventException(
                    "Missing required fields in TweetEventData"
            );
        }
    }

    private void handleEvent(TweetEvent event) {
        try {

            if (event == null || event.data().tweetId() == null || event.data().authorId() == null) {
                throw new IllegalArgumentException("tweetData, tweetId or authorId is missing");
            }

            log.info(
                    "Handling TweetEvent | eventId={} | eventType={} | tweetId={} | authorId={}",
                    event.eventId(),
                    event.eventType(),
                    event.data().tweetId(),
                    event.data().authorId()
            );

            feedService.handleNewTweet(event.data().tweetId(),
                    event.data().tweetType(),
                    event.data().authorId(),
                    event.data().rootTweetId(),
                    event.data().occurredAt());


        } catch (IllegalArgumentException e) {
            throw new NonRetryableEventException(e.getMessage());
        } catch (Exception e) {
            throw new RetryableEventException(e.getMessage());
        }
    }
}
