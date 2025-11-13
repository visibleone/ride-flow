package com.example.userservice.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.api.driver.model.DriverPayload;
import com.example.api.user.model.UserCreateRequest;
import com.example.api.user.model.UserPayload;
import com.example.userservice.client.DriverClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser(
    username = "test-user",
    roles = {"driver"})
@ActiveProfiles("test")
class UserServiceIntegrationTest {
  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  // TODO: Here mocking the external call to driver-service. Use wiremock instead.
  @MockitoBean private DriverClient driverClient;

  @Test
  void createUser_shouldReturn201_andPersistedUser() throws Exception {
    // arrange
    UserCreateRequest request =
        new UserCreateRequest(
            UUID.randomUUID(), "alice@example.com", "Alice", UserCreateRequest.RoleEnum.RIDER);

    // act & assert
    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.email").value("alice@example.com"))
        .andExpect(jsonPath("$.name").value("Alice"))
        .andExpect(jsonPath("$.role").value(UserPayload.RoleEnum.RIDER.toString()));
  }

  @Test
  void getUserById_shouldReturn200_forExistingUser() throws Exception {
    // create a user first
    UserPayload created = create("bob@example.com", "Bob", UserCreateRequest.RoleEnum.DRIVER);

    // act & assert
    mockMvc
        .perform(get("/users/" + created.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(created.getId().toString()))
        .andExpect(jsonPath("$.email").value("bob@example.com"))
        .andExpect(jsonPath("$.name").value("Bob"))
        .andExpect(jsonPath("$.role").value(UserPayload.RoleEnum.DRIVER.toString()));
  }

  @Test
  void listUsers_shouldReturn200_andIncludeCreatedUsers() throws Exception {
    // create two users
    UserPayload u1 = create("carol@example.com", "Carol", UserCreateRequest.RoleEnum.RIDER);
    UserPayload u2 = create("dave@example.com", "Dave", UserCreateRequest.RoleEnum.DRIVER);

    // act & assert
    mockMvc
        .perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[?(@.id == '" + u1.getId() + "')]").exists())
        .andExpect(jsonPath("$[?(@.id == '" + u2.getId() + "')]").exists());
  }

  @Test
  void getUserById_shouldReturn404_forMissingUser() throws Exception {
    // act & assert
    mockMvc.perform(get("/users/" + UUID.randomUUID())).andExpect(status().isNotFound());
  }

  private UserPayload create(String email, String name, UserCreateRequest.RoleEnum role)
      throws Exception {
    UserCreateRequest req = new UserCreateRequest(UUID.randomUUID(), email, name, role);

    when(driverClient.createDriver(any())).thenReturn(new DriverPayload());

    String responseJson =
        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readValue(responseJson, UserPayload.class);
  }
}
