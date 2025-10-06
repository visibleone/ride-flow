package com.example.userservice.controller;

import com.example.userservice.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UsersApi {
  private final UserService userService;

  public ResponseEntity<UserRequest> createUser(UserCreateRequest userCreateRequest) {
    log.debug("createUser: {}", userCreateRequest);
    UserRequest userRequest = userService.createUser(userCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(userRequest);
  }

  public ResponseEntity<UserRequest> getUserById(UUID id) {
    log.debug("getUserById: {}", id);
    UserRequest userRequest = userService.getUser(id);

    return ResponseEntity.ok(userRequest);
  }

  public ResponseEntity<List<UserRequest>> listUsers() {
    log.debug("listUsers");
    List<UserRequest> userRequests = userService.getAll();

    return ResponseEntity.ok(userRequests);
  }
}
