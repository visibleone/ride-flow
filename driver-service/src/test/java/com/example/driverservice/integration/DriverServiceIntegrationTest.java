package com.example.driverservice.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.driverservice.model.Driver;
import com.example.driverservice.model.DriverStatus;
import com.example.driverservice.repository.DriverRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.openapitools.model.DriverCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
  @MockitoBean private DriverRepository driverRepository;

  @Test
  void createDriver_shouldReturn201_andPersistedDriver() throws Exception {
    // arrange
    UUID userId = UUID.randomUUID();
    DriverCreateRequest req = new DriverCreateRequest(userId, "Toyota Prius", "ABC-123");

    when(driverRepository.save(any(Driver.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

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

    Driver d2 = new Driver();
    d2.setId(UUID.randomUUID());
    d2.setUserId(UUID.randomUUID());
    d2.setVehicle("Car B");
    d2.setLicenseNumber("LIC-2");
    d2.setStatus(DriverStatus.AVAILABLE);

    when(driverRepository.findByStatusAndDriverLocationNear(
            eq(DriverStatus.AVAILABLE), any(), any()))
        .thenReturn(Arrays.asList(d1, d2));

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
}
