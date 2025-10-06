package com.example.userservice.service;

import java.util.List;
import java.util.UUID;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserRequest;

public interface UserService {
  UserRequest createUser(UserCreateRequest request);

  UserRequest getUser(UUID id);

  List<UserRequest> getAll();
}
