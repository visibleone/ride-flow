package com.example.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@RequiredArgsConstructor
public class UserServiceConfiguration {
  public static final String REALM_ACCESS_CLAIM = "realm_access";
  public static final String ROLE_CLAIM_KEY = "roles";
  public static final String ROLE_PREFIX = "ROLE_";

  private final UserServiceConfigurationProperties properties;

  @Bean
  // Required to have the correct API version in the Swagger UI
  public OpenAPI overrideOpenApiInfoSpec() {
    return new OpenAPI()
        .info(
            new Info()
                .title(properties.getApiTitle())
                .description(properties.getApiDescription())
                .version(properties.getApiVersion()));
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        UserServiceConfiguration::getGrantedAuthoritiesFromRoles);

    return jwtAuthenticationConverter;
  }

  private static List<GrantedAuthority> getGrantedAuthoritiesFromRoles(Jwt jwt) {
    Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
    if (realmAccess != null && realmAccess.containsKey(ROLE_CLAIM_KEY)) {
      List<?> roles = (List<?>) realmAccess.get(ROLE_CLAIM_KEY);
      return roles.stream()
          .map(
              role ->
                  new org.springframework.security.core.authority.SimpleGrantedAuthority(
                      ROLE_PREFIX + role))
          .collect(java.util.stream.Collectors.toList());
    }

    return java.util.Collections.emptyList();
  }
}
