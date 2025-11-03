package com.example.driverservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "driver-service")
public class DriverServiceConfigurationProperties {
  private String apiVersion;
  private String apiTitle;
  private String apiDescription;
}
