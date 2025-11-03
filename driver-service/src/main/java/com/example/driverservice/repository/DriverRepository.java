package com.example.driverservice.repository;

import com.example.driverservice.model.Driver;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<Driver, UUID> {}
