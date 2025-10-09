package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    // Disable CSRF as the authentication here is based on JWT tokens, not sessions.
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            ex ->
                ex.pathMatchers("/api/users")
                    .permitAll() // Keycloak webhook
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
