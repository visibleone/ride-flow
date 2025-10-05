package com.example.userservice.service;

import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserRequest createUser(UserCreateRequest request) {
    User user = userMapper.userCreateRequestToUser(request);
    User savedUser = userRepository.save(user);

    return userMapper.userToUserRequest(savedUser);
  }

  public UserRequest getUser(UUID id) {
    return userRepository
        .findById(id)
        .map(userMapper::userToUserRequest)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  public List<UserRequest> getAll() {
    return userRepository.findAll().stream().map(userMapper::userToUserRequest).toList();
  }
}
