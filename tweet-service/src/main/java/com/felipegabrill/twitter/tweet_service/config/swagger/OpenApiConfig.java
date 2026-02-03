package com.felipegabrill.twitter.tweet_service.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tweetServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Twitter Clone - Tweet Service API")
                        .description("""
                                Microservice responsible for tweet management in the Twitter Clone platform.

                                Features:
                                - Create tweets (text and/or media)
                                - Reply to tweets
                                - Quote tweets
                                - Retweet
                                - Like and unlike tweets
                                - Fetch tweets by id
                                - Tweet domain validations and business rules
                                """)
                        .version("v1")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi tweetServiceApi() {
        return GroupedOpenApi.builder()
                .group("tweet-service")
                .packagesToScan(
                        "com.felipegabrill.twitter.tweet_service.controller",
                        "com.felipegabrill.twitter.tweet_service.dtos"
                )
                .pathsToMatch("/api/**")
                .build();
    }
}
