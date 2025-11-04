package com.example.driverservice.mapper;

import com.example.driverservice.model.Driver;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.openapitools.model.DriverCreateRequest;
import org.openapitools.model.DriverPayload;

@Mapper(componentModel = "spring")
public interface DriverMapper {

  @Mapping(target = "id", source = "request", qualifiedByName = "generateId")
  @Mapping(target = "driverLocation", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  Driver driverCreateRequestToDriver(DriverCreateRequest request);

  DriverPayload driverToDriverPayload(Driver savedDriver);

  default OffsetDateTime instantToOffsetDateTime(Instant instant) {
    return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
  }

  @Named("generateId")
  default UUID generateId(DriverCreateRequest request) {
    return UUID.randomUUID();
  }
}
