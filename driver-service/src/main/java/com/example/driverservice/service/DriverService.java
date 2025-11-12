package com.example.driverservice.service;

import com.example.api.driver.model.AvailabilityResponse;
import com.example.api.driver.model.DriverCreateRequest;
import com.example.api.driver.model.DriverPayload;

public interface DriverService {
  DriverPayload createDriver(DriverCreateRequest request);

  AvailabilityResponse checkAvailability(Double lat, Double lng, Double radius);
}
