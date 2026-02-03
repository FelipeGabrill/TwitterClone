package com.felipegabrill.twitter.tweet_service.config.converts.usermentions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

public record StringToUserMentionConverter(ObjectMapper objectMapper) implements Converter<String, UserMentionDTO> {

    @Override
    public UserMentionDTO convert(String source) {
        if (source.isBlank() || source.equals("{}")) {
            return null;
        }
        try {
            if (source.trim().startsWith("{")) {
                return objectMapper.readValue(source, UserMentionDTO.class);
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}