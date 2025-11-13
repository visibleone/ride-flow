package com.example.userservice.mapper;

import com.example.api.driver.model.DriverCreateRequest;
import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import com.example.userservice.model.User;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "id", ignore = true)
  User userCreateRequestToUser(UserCreateRequest request);

  UserPayload userToUserRequest(User user);

  @Mapping(target = "userId", source = "userId")
  DriverCreateRequest userPayloadToDriverCreateRequest(UUID userId, UserCreateRequest userPayload);

  default OffsetDateTime instantToOffsetDateTime(Instant instant) {
    return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
  }
}
