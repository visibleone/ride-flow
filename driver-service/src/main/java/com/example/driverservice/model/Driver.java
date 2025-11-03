package com.example.driverservice.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class Driver {
  @Id private UUID id;
  private UUID userId;
  private String vehicle;
  private String licenseNumber;

  @Field("location")
  @GeoSpatialIndexed
  private DriverLocation driverLocation;

  private DriverStatus status;
  @CreatedDate private Instant createdAt;
  @LastModifiedDate private Instant updatedAt;
}
