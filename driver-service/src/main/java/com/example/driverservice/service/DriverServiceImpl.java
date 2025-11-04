package com.example.driverservice.service;

import com.example.driverservice.mapper.DriverMapper;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.DriverCreateRequest;
import org.openapitools.model.DriverPayload;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;

  @Override
  public DriverPayload createDriver(DriverCreateRequest request) throws DuplicateKeyException {
    Driver driver = driverMapper.driverCreateRequestToDriver(request);
    Driver savedDriver = driverRepository.save(driver);

    return driverMapper.driverToDriverPayload(savedDriver);
  }
}
