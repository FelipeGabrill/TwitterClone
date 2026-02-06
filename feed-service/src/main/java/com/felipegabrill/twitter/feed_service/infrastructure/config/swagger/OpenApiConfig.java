package com.felipegabrill.twitter.feed_service.infrastructure.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI feedServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Twitter Clone - Feed Service API")
                        .description("""
                                Microservice responsible for feed generation and pagination 
                                in the Twitter Clone platform.

                                Features:
                                - Fetch user personalized feed
                                - Fetch global feed
                                - Cursor-based pagination
                                - Optimized DynamoDB access (single-page reads)
                                - Feed ordering
                                """)
                        .version("v1")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi feedServiceApi() {
        return GroupedOpenApi.builder()
                .group("feed-service")
                .packagesToScan(
                        "com.felipegabrill.twitter.feed_service.controllers",
                        "com.felipegabrill.twitter.feed_service.dtos"
                )
                .pathsToMatch("/api/**")
                .build();
    }
}
