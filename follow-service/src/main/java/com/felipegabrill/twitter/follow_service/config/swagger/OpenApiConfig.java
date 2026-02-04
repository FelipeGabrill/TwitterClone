package com.felipegabrill.twitter.follow_service.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI followServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Twitter Clone - Follow Service API")
                        .description("""
                                Microservice responsible for managing user follow relationships in the Twitter Clone platform.

                                Features:
                                - Follow a user
                                - Unfollow a user
                                - List users a given user is following
                                - List followers of a given user
                                - Check if a user is following another user
                                - Validations and business rules for follow operations
                                """)
                        .version("v1")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi followServiceApi() {
        return GroupedOpenApi.builder()
                .group("follow-service")
                .packagesToScan(
                        "com.felipegabrill.twitter.follow_service.controller",
                        "com.felipegabrill.twitter.follow_service.dtos"
                )
                .pathsToMatch("/api/**")
                .build();
    }
}
