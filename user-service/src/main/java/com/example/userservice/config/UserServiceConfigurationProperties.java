package com.example.userservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "user-service")
public class UserServiceConfigurationProperties {
  private String apiVersion;
  private String apiTitle;
  private String apiDescription;
}
