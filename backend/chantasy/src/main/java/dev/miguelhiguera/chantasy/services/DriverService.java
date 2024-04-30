package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.DriverDto;
import dev.miguelhiguera.chantasy.entities.Driver;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    Optional<Driver> getDriver(Long id) throws EntityNotFoundException;
    List<Driver> allDrivers();
    Driver createDriver(DriverDto input) throws EntityExistsException, EntityNotFoundException;
    Driver updateDriver(Long id, DriverDto input) throws EntityNotFoundException;
    void deleteDriver(Long id) throws EntityNotFoundException;
}
