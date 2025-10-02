package com.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.UsersApi;
import org.openapitools.model.User;
import org.openapitools.model.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UsersApi {

    public ResponseEntity<User> createUser(UserCreateRequest userCreateRequest) {
        log.info("createUser: {}", userCreateRequest);
        return null;
    }

    public ResponseEntity<User> getUserById(UUID id) {
        log.info("getUserById: {}", id);
        return null;
    }

    public ResponseEntity<List<User>> listUsers() {
        log.info("listUsers");
        return null;
    }

}
