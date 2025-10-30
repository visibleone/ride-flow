package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        // User service
        .route(
            "user-service",
            r ->
                r.path("/api/v1/users/**")
                    .filters(
                        f ->
                            f.circuitBreaker(
                                config ->
                                    config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-service")))
                    .uri("http://localhost:8082"))
        .build();
  }
}
