package com.example.driverservice.service;

import com.example.driverservice.mapper.DriverMapper;
import com.example.driverservice.model.Driver;
import com.example.driverservice.model.DriverStatus;
import com.example.driverservice.repository.DriverRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.AvailabilityResponse;
import org.openapitools.model.DriverCreateRequest;
import org.openapitools.model.DriverPayload;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
  public static final double DEFAULT_RADIUS = 5.0;
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;

  @Override
  public DriverPayload createDriver(DriverCreateRequest request) {
    Driver driver = driverMapper.driverCreateRequestToDriver(request);
    Driver savedDriver = driverRepository.save(driver);

    return driverMapper.driverToDriverPayload(savedDriver);
  }

  @Override
  public AvailabilityResponse checkAvailability(Double lat, Double lng, Double radius) {
    double effectiveRadiusKm = (radius == null ? DEFAULT_RADIUS : radius);

    Point point = new Point(lng, lat);
    Distance max = new Distance(effectiveRadiusKm, Metrics.KILOMETERS);

    List<Driver> nearbyAvailable =
        driverRepository.findByStatusAndDriverLocationNear(DriverStatus.AVAILABLE, point, max);

    int count = nearbyAvailable.size();

    AvailabilityResponse response = new AvailabilityResponse();
    response.setAvailable(count > 0);
    response.setCount(count);

    return response;
  }
}
