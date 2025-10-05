package com.example.userservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID keycloakId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String role; // RIDER or DRIVER

  @Column(nullable = false)
  private Instant createdAt = Instant.now();
}
