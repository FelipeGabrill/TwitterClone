package com.felipegabrill.twitter.user_service.application.commands;

public record CreateUserCommand(
        String username,
        String name,
        String bio,
        String location,
        String profileImageUrl,
        String bannerImageUrl
) {}
