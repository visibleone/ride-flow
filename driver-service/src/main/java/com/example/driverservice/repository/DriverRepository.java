package com.example.driverservice.repository;

import com.example.driverservice.model.Driver;
import com.example.driverservice.model.DriverStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<Driver, UUID> {
  List<Driver> findByStatusAndDriverLocationNear(
      DriverStatus status, Point point, Distance maxDistance);
}
