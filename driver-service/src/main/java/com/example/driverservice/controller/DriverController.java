package com.example.driverservice.controller;

import com.example.driverservice.service.DriverService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.DriversApi;
import org.openapitools.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DriverController implements DriversApi {
  private final DriverService driverService;

  @Override
  public ResponseEntity<AvailabilityResponse> checkAvailability(
      Double lat, Double lng, Double radius) {
    return DriversApi.super.checkAvailability(lat, lng, radius);
  }

  @Override
  public ResponseEntity<Driver> createDriver(CreateDriver createDriver) {
    return DriversApi.super.createDriver(createDriver);
  }

  @Override
  public ResponseEntity<Driver> getDriver(UUID id) {
    return DriversApi.super.getDriver(id);
  }

  @Override
  public ResponseEntity<Void> releaseDriver(UUID id, DriverReleaseRequest driverReleaseRequest) {
    return DriversApi.super.releaseDriver(id, driverReleaseRequest);
  }

  @Override
  public ResponseEntity<Driver> updateDriverLocation(UUID id, GeoLocation geoLocation) {
    return DriversApi.super.updateDriverLocation(id, geoLocation);
  }

  @Override
  public ResponseEntity<Driver> updateDriverStatus(
      UUID id, UpdateDriverStatusRequest updateDriverStatusRequest) {
    return DriversApi.super.updateDriverStatus(id, updateDriverStatusRequest);
  }
}
