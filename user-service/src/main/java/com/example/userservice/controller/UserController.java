package com.example.userservice.controller;

import com.example.api.user.api.UsersApi;
import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import com.example.userservice.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
// TODO: Consider introducing driver and rider sub-resources, like: /users/{id}/driver
// TODO: Make POST idempotent with idempotency key = keycloak id
public class UserController implements UsersApi {
  private final UserService userService;

  @Override
  public ResponseEntity<UserPayload> createUser(UserCreateRequest userCreateRequest) {
    log.debug("createUser: {}", userCreateRequest);
    UserPayload userPayload = userService.createUser(userCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(userPayload);
  }

  @Override
  public ResponseEntity<UserPayload> getUserById(UUID id) {
    log.debug("getUserById: {}", id);
    UserPayload userPayload = userService.getUser(id);

    return ResponseEntity.ok(userPayload);
  }

  // TODO: Remove authority check (TEST ONLY)
  @PreAuthorize("hasAuthority('ROLE_driver')")
  @Override
  public ResponseEntity<List<UserPayload>> listUsers() {
    log.debug("listUsers");
    List<UserPayload> userPayloads = userService.getAll();

    return ResponseEntity.ok(userPayloads);
  }
}
