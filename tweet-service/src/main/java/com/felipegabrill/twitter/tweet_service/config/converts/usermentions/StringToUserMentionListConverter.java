package com.felipegabrill.twitter.tweet_service.config.converts.usermentions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.dtos.usermention.UserMentionDTO;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record StringToUserMentionListConverter(
        ObjectMapper objectMapper) implements Converter<String, List<UserMentionDTO>> {

    @Override
    public List<UserMentionDTO> convert(String source) {
        if (source.isBlank() || source.equals("{}")) {
            return new ArrayList<>();
        }
        try {
            if (source.trim().startsWith("[")) {
                return objectMapper.readValue(source, new TypeReference<List<UserMentionDTO>>() {
                });
            }
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}