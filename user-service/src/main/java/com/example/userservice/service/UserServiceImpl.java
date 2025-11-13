package com.example.userservice.service;

import com.example.api.driver.model.DriverCreateRequest;
import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import com.example.userservice.client.DriverClient;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final DriverClient driverClient;

  @Override
  @Transactional
  public UserPayload createUser(UserCreateRequest request) {
    User user = userMapper.userCreateRequestToUser(request);
    User savedUser = userRepository.save(user);

    if (UserRole.DRIVER.equals(savedUser.getRole())) {
      DriverCreateRequest driverCreateRequest =
          userMapper.userPayloadToDriverCreateRequest(savedUser.getId(), request);
      driverClient.createDriver(driverCreateRequest);
    }

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
