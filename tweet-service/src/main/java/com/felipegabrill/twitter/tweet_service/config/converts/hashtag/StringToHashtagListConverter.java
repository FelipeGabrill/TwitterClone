package com.felipegabrill.twitter.tweet_service.config.converts.hashtag;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.dtos.hashtag.HashtagDTO;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record StringToHashtagListConverter(
        ObjectMapper objectMapper) implements Converter<String, List<HashtagDTO>> {

    @Override
    public List<HashtagDTO> convert(String source) {
        if (source.isBlank() || source.equals("{}")) {
            return new ArrayList<>();
        }
        try {
            if (source.trim().startsWith("[")) {
                return objectMapper.readValue(source, new TypeReference<List<HashtagDTO>>() {
                });
            }
            HashtagDTO dto = new HashtagDTO();
            dto.setText(source);
            dto.setStartIndex(0);
            dto.setEndIndex(source.length());
            List<HashtagDTO> list = new ArrayList<>();
            list.add(dto);
            return list;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
