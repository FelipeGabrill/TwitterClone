package com.felipegabrill.twitter.user_service.adapters.inbound.controllers.impl;

import com.felipegabrill.twitter.user_service.adapters.inbound.controllers.UserController;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserPreviewDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UserResponseDTO;
import com.felipegabrill.twitter.user_service.application.usecases.UserUseCases;
import com.felipegabrill.twitter.user_service.domain.user.projections.UserPreviewProjection;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
public class UserControllerImpl implements UserController {

    private final UserUseCases userUseCases;

    public UserControllerImpl(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @Override
    public ResponseEntity<UserResponseDTO> insert(@Valid @ModelAttribute CreateUserDTO dto) {
        UserResponseDTO newDto = userUseCases.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getByUsername(
            @PathVariable String username) {
        UserResponseDTO dto = userUseCases.getByUsername(username);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        UserResponseDTO dto = userUseCases.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @Valid @ModelAttribute UpdateUserDTO dto) {
        UserResponseDTO newDto = userUseCases.updateProfile(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Override
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        userUseCases.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> incrementFollowing(@PathVariable UUID id) {
        userUseCases.incrementFollowersCount(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> decrementFollowing(@PathVariable UUID id) {
        userUseCases.decrementFollowersCount(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<UserPreviewDTO>> searchUsers(
            @RequestParam(required = false)  String username,
            @RequestParam(required = false)  String name,
            Pageable pageable
    ) {
        Page<UserPreviewDTO> result = userUseCases.searchUsers(username, name, pageable);

        return ResponseEntity.ok(result);
    }
}
