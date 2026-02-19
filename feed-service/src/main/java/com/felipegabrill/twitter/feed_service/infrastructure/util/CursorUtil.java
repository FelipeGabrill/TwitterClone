package com.felipegabrill.twitter.feed_service.infrastructure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.feed_service.infrastructure.util.exception.CursorDecodeException;
import com.felipegabrill.twitter.feed_service.infrastructure.util.exception.CursorEncodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CursorUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(CursorUtil.class);

    public static String encode(Map<String, AttributeValue> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            Map<String, String> temp = new HashMap<>();
            map.forEach((k, v) -> temp.put(k, v.s()));
            String json = mapper.writeValueAsString(temp);
            return Base64.getEncoder().encodeToString(json.getBytes());
        } catch (Exception e) {
            log.error("Failed to encode pagination cursor", e);
            throw new CursorEncodeException("Failed to encode pagination cursor");
        }
    }

    public static Map<String, AttributeValue> decode(String cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            String json = new String(Base64.getDecoder().decode(cursor));
            Map<String, String> temp = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            Map<String, AttributeValue> result = new HashMap<>();
            temp.forEach((k, v) -> result.put(k, AttributeValue.builder().s(v).build()));
            return result;
        } catch (Exception e) {
            log.warn("Invalid pagination cursor received");
            log.debug("Cursor decoding error details", e);
            throw new CursorDecodeException("Invalid or corrupted pagination cursor");
        }
    }

    public static String encode(TweetCursor cursor) {
        if (cursor == null) return null;
        try {
            String json = mapper.writeValueAsString(Map.of(
                    "createdAt", cursor.createdAt().toString(),
                    "tweetId", cursor.tweetId().toString()
            ));
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Failed to encode RDS pagination cursor", e);
            throw new CursorEncodeException("Failed to encode RDS pagination cursor");
        }
    }

    public static TweetCursor decodeRds(String cursorStr) {
        if (cursorStr == null) return null;
        try {
            String json = new String(Base64.getDecoder().decode(cursorStr), StandardCharsets.UTF_8);
            Map<String, String> temp = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            return new TweetCursor(
                    Instant.parse(temp.get("createdAt")),
                    UUID.fromString(temp.get("tweetId"))
            );
        } catch (Exception e) {
            log.warn("Invalid RDS pagination cursor received", e);
            throw new CursorDecodeException("Invalid or corrupted RDS pagination cursor");
        }
    }
}
