package com.example.userservice.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRetryConfiguration {

  @Bean
  // This creates exponential backoff: 1s, 1.5s, 2.25s, etc. up to 5s max
  public Retryer feignRetryer() {
    return new Retryer.Default(1000, 5000, 3);
  }
}
