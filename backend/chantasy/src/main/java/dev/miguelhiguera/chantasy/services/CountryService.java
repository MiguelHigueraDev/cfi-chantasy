package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CountryService {
    Optional<Country> getCountry(Long id) throws EntityNotFoundException;
    Page<Country> allCountries(Pageable pageable);
    Country createCountry(CountryDto input) throws EntityExistsException;
    Country updateCountry(Long id, CountryDto input) throws EntityNotFoundException;
    void deleteCountry(Long id) throws EntityNotFoundException;
}
