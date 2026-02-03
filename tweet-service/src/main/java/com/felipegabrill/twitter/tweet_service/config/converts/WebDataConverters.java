package com.felipegabrill.twitter.tweet_service.config.converts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabrill.twitter.tweet_service.config.converts.hashtag.StringToHashtagConverter;
import com.felipegabrill.twitter.tweet_service.config.converts.hashtag.StringToHashtagListConverter;
import com.felipegabrill.twitter.tweet_service.config.converts.usermentions.StringToUserMentionConverter;
import com.felipegabrill.twitter.tweet_service.config.converts.usermentions.StringToUserMentionListConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebDataConverters implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebDataConverters(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToHashtagConverter(objectMapper));
        registry.addConverter(new StringToHashtagListConverter(objectMapper));

        registry.addConverter(new StringToUserMentionConverter(objectMapper));
        registry.addConverter(new StringToUserMentionListConverter(objectMapper));
    }
}
