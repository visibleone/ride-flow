package com.example.userservice.service;

import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import java.util.List;
import java.util.UUID;

public interface UserService {
  UserPayload createUser(UserCreateRequest request);

  UserPayload getUser(UUID id);

  List<UserPayload> getAll();
}
