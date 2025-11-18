package com.example.gatewayservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway-service")
@Data
public class GatewayServiceConfigurationProperties {
  private String apiPathVersion;
  private String usersServiceUrl;
}
