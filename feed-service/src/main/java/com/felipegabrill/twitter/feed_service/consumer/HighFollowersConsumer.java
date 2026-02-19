package com.felipegabrill.twitter.feed_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.feed_service.consumer.events.highfollowers.HighFollowersEvent;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.InvalidEventException;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.NonRetryableEventException;
import com.felipegabrill.twitter.feed_service.consumer.exceptions.RetryableEventException;
import com.felipegabrill.twitter.feed_service.services.IFamousUserService;
import com.felipegabrill.twitter.feed_service.services.exceptions.FollowPersistenceException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HighFollowersConsumer {

    private static final Logger log = LoggerFactory.getLogger(HighFollowersConsumer.class);
    private final IFamousUserService famousUserService;
    private final ObjectMapper objectMapper;

    public HighFollowersConsumer(IFamousUserService famousUserService, ObjectMapper objectMapper) {
        this.famousUserService = famousUserService;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${aws.sqs.high-followers-queue-url}")
    public void receiveMessage(String message) {
        try {
            HighFollowersEvent event = parseEvent(message);

            log.info("Processing HighFollowersEvent | eventType={} | userId={} | followers={}",
                    event.eventType(),
                    event.data().userId(),
                    event.data().followersCount()
            );

            handleEvent(event);

        } catch (InvalidEventException e) {
            log.error("Non-retryable error while processing HighFollowersEvent | reason={}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while processing HighFollowersEvent", e);
            throw new RetryableEventException("Unexpected error processing HighFollowersEvent");
        }
    }

    private HighFollowersEvent parseEvent(String message) {
        try {
            JsonNode snsEnvelope = objectMapper.readTree(message);
            String payload = snsEnvelope.path("Message").asText("");

            if (payload.isBlank()) {
                throw new NonRetryableEventException("SNS envelope missing or empty 'Message'");
            }

            HighFollowersEvent event = objectMapper.readValue(payload, HighFollowersEvent.class);
            validate(event);

            return event;
        } catch (JsonProcessingException e) {
            throw new NonRetryableEventException("Failed to deserialize HighFollowersEvent");
        }
    }

    private void validate(HighFollowersEvent event) {
        if (event.eventType() == null ||
                event.data() == null ||
                event.data().userId() == null) {
            throw new NonRetryableEventException("Missing required fields in HighFollowersEvent");
        }
    }

    private void handleEvent(HighFollowersEvent event) {
        try {
            famousUserService.promoteToFamous(
                    event.data().userId(),
                    event.data().followersCount()
            );
            log.info("User promoted to famous successfully | userId={}", event.data().userId());
        } catch (IllegalArgumentException e) {
            throw new InvalidEventException(e.getMessage());
        } catch (FollowPersistenceException e) {
            throw new RetryableEventException(e.getMessage());
        }
    }
}