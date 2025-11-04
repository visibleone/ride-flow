package com.example.driverservice.service;

import org.openapitools.model.DriverCreateRequest;
import org.openapitools.model.DriverPayload;

public interface DriverService {
  DriverPayload createDriver(DriverCreateRequest request);
}
