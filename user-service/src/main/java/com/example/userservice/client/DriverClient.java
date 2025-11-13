package com.example.userservice.client;

import com.example.api.driver.model.DriverCreateRequest;
import com.example.api.driver.model.DriverPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "driver-service")
public interface DriverClient {
  @PostMapping(value = "/api/v1/drivers", consumes = "application/json")
  DriverPayload createDriver(DriverCreateRequest driverCreateRequest);
}
