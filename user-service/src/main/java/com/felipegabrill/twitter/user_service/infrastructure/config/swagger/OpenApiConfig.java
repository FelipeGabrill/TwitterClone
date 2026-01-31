package com.felipegabrill.twitter.user_service.infrastructure.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Twitter Clone - User Service API")
                        .description("""
                                Microservice responsible for user management in the Twitter Clone platform.

                                Features:
                                - User registration
                                - Profile update
                                - Profile and banner image upload and removal
                                - User domain validations and business rules
                                """)
                        .version("v1")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .packagesToScan(
                        "com.felipegabrill.twitter.user_service.adapters.inbound"
                )
                .pathsToMatch("/api/**")
                .build();
    }
}
