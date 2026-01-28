package com.felipegabrill.twitter.user_service.adapters.inbound.controllers;

import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.CreateUserDTO;
import com.felipegabrill.twitter.user_service.adapters.inbound.dtos.UpdateUserDTO;
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
@RequestMapping("api/v1/users")
public class UserController {

    private final UserUseCases userUseCases;

    public UserController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody CreateUserDTO dto) {
        UserResponseDTO newDto = userUseCases.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getByLogin(
            @PathVariable String username) {
        UserResponseDTO dto = userUseCases.getByUsername(username);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        UserResponseDTO dto = userUseCases.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserDTO dto) {
        UserResponseDTO newDto = userUseCases.updateProfile(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        userUseCases.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/follow")
    public ResponseEntity<Void> incrementFollowing(@PathVariable UUID id) {
        userUseCases.incrementFollowersCount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/unfollow")
    public ResponseEntity<Void> decrementFollowing(@PathVariable UUID id) {
        userUseCases.decrementFollowersCount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserPreviewProjection>> searchUsers(
            @RequestParam(required = false)  String username,
            @RequestParam(required = false)  String name,
            Pageable pageable
    ) {
        Page<UserPreviewProjection> result = userUseCases.searchUsers(username, name, pageable);
        return ResponseEntity.ok(result);
    }
}
