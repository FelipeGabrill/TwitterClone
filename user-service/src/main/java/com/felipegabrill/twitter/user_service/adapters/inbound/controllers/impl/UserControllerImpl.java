package com.felipegabrill.twitter.user_service.adapters.inbound.controllers.impl;

import com.felipegabrill.twitter.user_service.adapters.inbound.controllers.UserController;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
public class UserControllerImpl implements UserController {

    private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);

    private final UserUseCases userUseCases;

    public UserControllerImpl(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @Override
    public ResponseEntity<UserResponseDTO> insert(@Valid @ModelAttribute CreateUserDTO dto) {
        log.info("Request to create user with username={}", dto.getUsername());

        UserResponseDTO newDto = userUseCases.create(dto);

        log.info("User created successfully with id={} and username={}",
                newDto.getId(), newDto.getUsername());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getByUsername(@PathVariable String username) {
        log.debug("Request to get user by username={}", username);

        UserResponseDTO dto = userUseCases.getByUsername(username);

        log.debug("User found for username={} with id={}", username, dto.getId());
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        log.debug("Request to get user by id={}", id);

        UserResponseDTO dto = userUseCases.getById(id);

        log.debug("User found with id={} and username={}", id, dto.getUsername());
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateUserDTO dto
    ) {
        log.info("Request to update user profile with id={}", id);

        UserResponseDTO updatedDto = userUseCases.updateProfile(id, dto);

        log.info("User profile updated successfully for id={}", id);
        return ResponseEntity.ok(updatedDto);
    }

    @Override
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        log.warn("Request to deactivate user with id={}", id);

        userUseCases.deactivate(id);

        log.warn("User deactivated successfully with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> incrementFollowing(@PathVariable UUID id) {
        log.debug("Increment followers count for user id={}", id);

        userUseCases.incrementFollowersCount(id);

        log.debug("Followers count incremented for user id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> decrementFollowing(@PathVariable UUID id) {
        log.debug("Decrement followers count for user id={}", id);

        userUseCases.decrementFollowersCount(id);

        log.debug("Followers count decremented for user id={}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<UserPreviewDTO>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        log.debug(
                "Search users request: username={}, name={}, page={}, size={}",
                username, name, pageable.getPageNumber(), pageable.getPageSize()
        );

        Page<UserPreviewDTO> result =
                userUseCases.searchUsers(username, name, pageable);

        log.debug("Search users result: totalElements={}", result.getTotalElements());

        return ResponseEntity.ok(result);
    }
}
