package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.services.CountryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Optional<Country> getCountry(Long id) throws EntityNotFoundException {
        Optional<Country> optionalCountry = countryRepository.findById(id);

        if (optionalCountry.isEmpty() || optionalCountry.get().isDeleted()) {
            throw new EntityNotFoundException("Country not found.");
        }

        return optionalCountry;
    }

    @Override
    public List<Country> allCountries() {
        List<Country> countries = new ArrayList<>();
        countryRepository.findAll().forEach(country -> {
            if (!country.isDeleted()) {
                countries.add(country);
            }
        });
        return countries;
    }

    @Override
    public Country createCountry(CountryDto input) {
        Optional<Country> optionalCountry = countryRepository.findByName(input.getName());

        if (optionalCountry.isPresent()) {
            throw new EntityExistsException("Country already exists.");
        }

        Country country = new Country();
        country.setName(input.getName());
        country.setCode(input.getCode());
        country.setFlagUrl(input.getFlagUrl());

        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Long id, CountryDto input) throws EntityNotFoundException {
        Optional<Country> optionalCountry = countryRepository.findById(id);

        if (optionalCountry.isEmpty() || optionalCountry.get().isDeleted()) {
            throw new EntityNotFoundException("Country not found.");
        }

        Country country = optionalCountry.get();
        country.setName(input.getName());
        country.setCode(input.getCode());
        country.setFlagUrl(input.getFlagUrl());

        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Long id) throws EntityNotFoundException {
        Optional<Country> optionalCountry = countryRepository.findById(id);

        if (optionalCountry.isEmpty()) {
            throw new EntityNotFoundException("Country not found.");
        }

        Country country = optionalCountry.get();
        country.setDeleted(true);
        countryRepository.save(country);
    }
}
