package com.example.userservice.repository;

import com.example.userservice.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByKeycloakId(UUID keycloakId);

  Optional<User> findByEmail(String email);
}
