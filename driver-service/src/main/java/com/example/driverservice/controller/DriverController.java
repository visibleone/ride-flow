package com.example.driverservice.controller;

import com.example.driverservice.service.DriverService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.DriversApi;
import org.openapitools.model.*;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<DriverPayload> createDriver(DriverCreateRequest createDriver) {
    log.debug("createDriver: {}", createDriver);
    DriverPayload driverPayload = driverService.createDriver(createDriver);

    return ResponseEntity.status(HttpStatus.CREATED).body(driverPayload);
  }

  @Override
  public ResponseEntity<DriverPayload> getDriver(UUID id) {
    return DriversApi.super.getDriver(id);
  }

  @Override
  public ResponseEntity<Void> releaseDriver(UUID id, DriverReleaseRequest driverReleaseRequest) {
    return DriversApi.super.releaseDriver(id, driverReleaseRequest);
  }

  @Override
  public ResponseEntity<DriverPayload> updateDriverLocation(UUID id, GeoLocation geoLocation) {
    return DriversApi.super.updateDriverLocation(id, geoLocation);
  }

  @Override
  public ResponseEntity<DriverPayload> updateDriverStatus(
      UUID id, UpdateDriverStatusRequest updateDriverStatusRequest) {
    return DriversApi.super.updateDriverStatus(id, updateDriverStatusRequest);
  }
}
