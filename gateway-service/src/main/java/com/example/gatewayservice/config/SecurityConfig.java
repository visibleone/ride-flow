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
  private static final String[] AUTH_WHITELIST = {
    "/actuator/**"
  };

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(
            exchanges ->
                exchanges.pathMatchers(AUTH_WHITELIST).permitAll().anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .csrf(ServerHttpSecurity.CsrfSpec::disable);
    return http.build();
  }
}
