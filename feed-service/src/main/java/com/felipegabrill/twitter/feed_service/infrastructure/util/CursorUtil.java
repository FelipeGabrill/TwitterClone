package com.felipegabrill.twitter.feed_service.infrastructure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CursorUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String encode(Map<String, AttributeValue> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            Map<String, String> temp = new HashMap<>();
            map.forEach((k, v) -> temp.put(k, v.s())); // apenas strings (PK/SK)
            String json = mapper.writeValueAsString(temp);
            return Base64.getEncoder().encodeToString(json.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode cursor", e);
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
            throw new RuntimeException("Failed to decode cursor", e);
        }
    }
}
