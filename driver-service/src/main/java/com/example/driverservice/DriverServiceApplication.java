package com.example.driverservice;

import com.example.driverservice.config.DriverServiceConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(DriverServiceConfigurationProperties.class)
public class DriverServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(DriverServiceApplication.class, args);
  }
}
