package com.example.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {
  private final GatewayServiceConfigurationProperties properties;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    String baseUsersPath = "/api/" + properties.getApiPathVersion() + "/users/**";

    return builder
        .routes()
        // User service
        .route(
            "user-service",
            r ->
                r.path(baseUsersPath)
                    .filters(
                        f ->
                            f.circuitBreaker(
                                config ->
                                    config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-service")))
                    .uri(properties.getUsersServiceUrl()))
        .build();
  }
}
