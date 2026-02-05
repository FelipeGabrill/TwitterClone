package com.felipegabrill.twitter.like_service.infrastructure.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI likeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Twitter Clone - Like Service API")
                        .description("""
                                Microservice responsible for managing likes in the Twitter Clone platform.

                                Responsibilities:
                                - Like a tweet
                                - Unlike a tweet
                                - Prevent duplicate likes
                                - Enforce business rules for like/unlike operations
                                """)
                        .version("v1")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi likeServiceApi() {
        return GroupedOpenApi.builder()
                .group("like-service")
                .packagesToScan(
                        "com.felipegabrill.twitter.like_service.adapters.inbound"
                )
                .pathsToMatch("/api/**")
                .build();
    }
}
