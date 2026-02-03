package com.felipegabrill.twitter.tweet_service.config.converts.hashtag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

public record StringToHashtagConverter(ObjectMapper objectMapper) implements Converter<String, HashtagDTO> {

    @Override
    public HashtagDTO convert(String source) {
        if (source.isBlank() || source.equals("{}")) {
            return null;
        }
        try {
            if (source.trim().startsWith("{")) {
                return objectMapper.readValue(source, HashtagDTO.class);
            }
            HashtagDTO dto = new HashtagDTO();
            dto.setText(source);
            dto.setStartIndex(0);
            dto.setEndIndex(source.length());
            return dto;
        } catch (IOException e) {
            return null;
        }
    }
}