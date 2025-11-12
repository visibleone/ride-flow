package com.example.userservice.service;

import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserPayload createUser(UserCreateRequest request) {
    User user = userMapper.userCreateRequestToUser(request);
    User savedUser = userRepository.save(user);

    // TODO: If DRIVER, create driver profile in driver-service

    return userMapper.userToUserRequest(savedUser);
  }

  @Override
  public UserPayload getUser(UUID id) {
    return userRepository
        .findById(id)
        .map(userMapper::userToUserRequest)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  @Override
  public List<UserPayload> getAll() {
    return userRepository.findAll().stream().map(userMapper::userToUserRequest).toList();
  }
}
