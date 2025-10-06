package com.example.userservice.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserServiceIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String baseUrl() {
    return "http://localhost:" + port + "/api/v1";
  }

  @Test
  void createUser_shouldReturn201_andPersistedUser() {
    // arrange
    UserCreateRequest req =
        new UserCreateRequest(
            UUID.randomUUID(), "alice@example.com", "Alice", UserCreateRequest.RoleEnum.RIDER);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // act
    ResponseEntity<UserRequest> response =
        restTemplate.postForEntity(
            baseUrl() + "/users", new HttpEntity<>(req, headers), UserRequest.class);

    // assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    UserRequest body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getId()).isNotNull();
    assertThat(body.getEmail()).isEqualTo("alice@example.com");
    assertThat(body.getName()).isEqualTo("Alice");
    assertThat(body.getRole()).isEqualTo(UserRequest.RoleEnum.RIDER);
  }

  @Test
  void getUserById_shouldReturn200_forExistingUser() {
    // create a user first
    UserRequest created = create("bob@example.com", "Bob", UserCreateRequest.RoleEnum.DRIVER);

    // act
    ResponseEntity<UserRequest> response =
        restTemplate.getForEntity(baseUrl() + "/users/" + created.getId(), UserRequest.class);

    // assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    UserRequest body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getId()).isEqualTo(created.getId());
    assertThat(body.getEmail()).isEqualTo("bob@example.com");
    assertThat(body.getRole()).isEqualTo(UserRequest.RoleEnum.DRIVER);
  }

  @Test
  void listUsers_shouldReturn200_andIncludeCreatedUsers() {
    // create two users
    UserRequest u1 = create("carol@example.com", "Carol", UserCreateRequest.RoleEnum.RIDER);
    UserRequest u2 = create("dave@example.com", "Dave", UserCreateRequest.RoleEnum.DRIVER);

    // act
    ResponseEntity<UserRequest[]> response =
        restTemplate.getForEntity(baseUrl() + "/users", UserRequest[].class);

    // assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    UserRequest[] users = response.getBody();
    assertThat(users).isNotNull();
    assertThat(List.of(users)).extracting(UserRequest::getId).contains(u1.getId(), u2.getId());
  }

  @Test
  void getUserById_shouldReturn404_forMissingUser() {
    // act
    ResponseEntity<String> response =
        restTemplate.getForEntity(baseUrl() + "/users/" + UUID.randomUUID(), String.class);

    // assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private UserRequest create(String email, String name, UserCreateRequest.RoleEnum role) {
    UserCreateRequest req = new UserCreateRequest(UUID.randomUUID(), email, name, role);
    ResponseEntity<UserRequest> response =
        restTemplate.postForEntity(baseUrl() + "/users", req, UserRequest.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    return response.getBody();
  }
}
