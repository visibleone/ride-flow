package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouteConfig {
  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        // User service (for Keycloak webhook + user APIs)
        .route(
            "user-service",
            r ->
                r.path("/api/users/**")
                    .filters(
                        f ->
                            f.circuitBreaker(
                                config ->
                                    config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-service")))
                    .uri("http://user-service:8080"))
        // Example: Ride service
        //        .route("ride-service", r ->
        // r.path("/api/rides/**").uri("http://ride-service:8080"))
        .build();
  }

  @Bean
  public RouterFunction<?> fallbackRoute() {
    return RouterFunctions.route()
        .GET(
            "/fallback/user-service",
            req ->
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Mono.just("User service unavailable"), String.class))
        .build();
  }
}
