package com.example.userservice.config;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignConfiguration {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication instanceof JwtAuthenticationToken jwtToken) {
        String token = jwtToken.getToken().getTokenValue();
        template.header("Authorization", "Bearer " + token);
      }
    };
  }

  @Bean
  // This creates exponential backoff: 1s, 1.5s, 2.25s, etc. up to 5s max
  public Retryer feignRetryer() {
    return new Retryer.Default(1000, 5000, 3);
  }
}
