package com.example.driverservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.driverservice.model.Driver;
import com.example.driverservice.model.DriverStatus;
import com.example.driverservice.repository.DriverRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.DriverCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class DriverServiceIntegrationTest {
  @Container @ServiceConnection static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private DriverRepository driverRepository;

  @BeforeEach
  void cleanDb() {
    driverRepository.deleteAll();
  }

  @Test
  void createDriver_shouldReturn201_andPersistedDriver() throws Exception {
    // arrange
    UUID userId = UUID.randomUUID();
    DriverCreateRequest req = new DriverCreateRequest(userId, "Toyota Prius", "ABC-123");

    // act & assert
    MvcResult result =
        mockMvc
            .perform(
                post("/drivers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.vehicle").value("Toyota Prius"))
            .andExpect(jsonPath("$.licenseNumber").value("ABC-123"))
            .andExpect(jsonPath("$.status").value("AVAILABLE"))
            .andExpect(jsonPath("$.location").doesNotExist())
            .andReturn();
  }

  @Test
  void checkAvailability_shouldReturn200_andCorrectCount() throws Exception {
    // arrange
    double lat = 37.7749;
    double lng = -122.4194;

    Driver d1 = new Driver();
    d1.setId(UUID.randomUUID());
    d1.setUserId(UUID.randomUUID());
    d1.setVehicle("Car A");
    d1.setLicenseNumber("LIC-1");
    d1.setStatus(DriverStatus.AVAILABLE);
    d1.setDriverLocation(new GeoJsonPoint(lng, lat));

    Driver d2 = new Driver();
    d2.setId(UUID.randomUUID());
    d2.setUserId(UUID.randomUUID());
    d2.setVehicle("Car B");
    d2.setLicenseNumber("LIC-2");
    d2.setStatus(DriverStatus.AVAILABLE);
    d2.setDriverLocation(new GeoJsonPoint(lng + 0.01, lat + 0.01));

    driverRepository.save(d1);
    driverRepository.save(d2);

    // act & assert
    mockMvc
        .perform(
            get("/drivers/availability")
                .param("lat", String.valueOf(lat))
                .param("lng", String.valueOf(lng))
                .param("radius", "5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.available").value(true))
        .andExpect(jsonPath("$.count").value(2));
  }

  @Test
  void createDriver_withDuplicateUserId_shouldReturn409_andErrorMessage() throws Exception {
    // arrange
    UUID userId = UUID.randomUUID();
    DriverCreateRequest req1 = new DriverCreateRequest(userId, "Tesla Model 3", "TES-123");

    // first creation succeeds
    mockMvc
        .perform(
            post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1)))
        .andExpect(status().isCreated());

    // second creation with same userId should fail with 409
    DriverCreateRequest req2 = new DriverCreateRequest(userId, "Toyota Corolla", "ABC-999");

    mockMvc
        .perform(
            post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("A driver with this userId already exists"));
  }
}
