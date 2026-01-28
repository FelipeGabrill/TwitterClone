package com.felipegabrill.twitter.user_service.application.commands;

import java.util.UUID;

public record UpdateUserCommand(
        UUID userId,
        String name,
        String bio,
        String location,
        String profileImageUrl,
        String bannerImageUrl
) {}
