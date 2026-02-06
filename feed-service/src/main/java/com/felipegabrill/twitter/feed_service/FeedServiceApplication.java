package com.felipegabrill.twitter.feed_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        io.awspring.cloud.autoconfigure.dynamodb.DynamoDbAutoConfiguration.class
})
public class FeedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedServiceApplication.class, args);
	}

}
