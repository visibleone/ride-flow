package com.example.userservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {
    @Value("${user-service.api.version}")
    private String apiVersion;

    @Value("${user-service.api.title}")
    private String apiTitle;

    @Value("${user-service.api.description}")
    private String apiDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion));
    }
}
