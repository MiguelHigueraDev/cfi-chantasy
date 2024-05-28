package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.DriverDto;
import dev.miguelhiguera.chantasy.entities.Driver;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DriverService {
    Optional<Driver> getDriver(Long id) throws EntityNotFoundException;
    Page<Driver> allDrivers(Pageable pageable);
    Driver createDriver(DriverDto input) throws EntityExistsException, EntityNotFoundException;
    Driver updateDriver(Long id, DriverDto input) throws EntityNotFoundException;
    void deleteDriver(Long id) throws EntityNotFoundException;
}
