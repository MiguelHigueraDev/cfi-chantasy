package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.DriverDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final CountryRepository countryRepository;
    private final TeamRepository teamRepository;

    public DriverServiceImpl(DriverRepository driverRepository, CountryRepository countryRepository, TeamRepository teamRepository) {
        this.driverRepository = driverRepository;
        this.countryRepository = countryRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Optional<Driver> getDriver(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        if (optionalDriver.isEmpty() || optionalDriver.get().isDeleted()) {
            throw new EntityNotFoundException("Driver not found.");
        }

        return optionalDriver;
    }

    @Override
    public List<Driver> allDrivers() {
        List<Driver> drivers = new ArrayList<>();
        driverRepository.findAll().forEach(driver -> {
            if (!driver.isDeleted()) {
                drivers.add(driver);
            }
        });
        return drivers;
    }

    @Override
    public Driver createDriver(DriverDto input) {
        Optional<Driver> optionalDriver = driverRepository.findByName(input.getName());
        Country country = getCountry(input.getCountryId());
        Team team = getTeam(input.getTeamId());

        if (optionalDriver.isPresent()) {
            throw new EntityExistsException("Driver already exists.");
        }

        Driver driver = new Driver();
        driver.setName(input.getName());
        driver.setCode(input.getCode());
        driver.setCountry(country);
        driver.setTeam(team);
        driver.setDeleted(false);

        return driverRepository.save(driver);
    }

    @Override
    public Driver updateDriver(Long id, DriverDto input) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        Country country = getCountry(input.getCountryId());
        Team team = getTeam(input.getTeamId());

        if (optionalDriver.isEmpty()) {
            throw new EntityNotFoundException("Driver not found.");
        }

        Driver driver = optionalDriver.get();
        driver.setName(input.getName());
        driver.setCode(input.getCode());
        driver.setCountry(country);
        driver.setTeam(team);
        return driverRepository.save(driver);
    }

    @Override
    public void deleteDriver(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);

        if (optionalDriver.isEmpty()) {
            throw new EntityNotFoundException("Driver not found.");
        }

        Driver driver = optionalDriver.get();
        driver.setDeleted(true);
        driverRepository.save(driver);
    }

    private Country getCountry(Long countryId) {
        Optional<Country> optionalCountry = countryRepository.findById(countryId);

        if (optionalCountry.isEmpty() || optionalCountry.get().isDeleted()) {
            throw new EntityNotFoundException("Invalid country ID.");
        }

        return optionalCountry.get();
    }

    private Team getTeam(Long teamId) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);

        if (optionalTeam.isEmpty() || optionalTeam.get().isDeleted()) {
            throw new EntityNotFoundException("Invalid team ID.");
        }

        return optionalTeam.get();
    }
}
