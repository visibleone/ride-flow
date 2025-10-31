package com.example.userservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @GeneratedValue(strategy = GenerationType.UUID)
  @Id
  private UUID id;

  @Column(nullable = false)
  // TODO: May be better to have this as primary key
  private UUID keycloakId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role; // RIDER or DRIVER

  @Column(nullable = false)
  private Instant createdAt = Instant.now();
}
