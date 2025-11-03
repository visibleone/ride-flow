package com.example.userservice.service;

import java.util.List;
import java.util.UUID;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserPayload;

public interface UserService {
  UserPayload createUser(UserCreateRequest request);

  UserPayload getUser(UUID id);

  List<UserPayload> getAll();
}
