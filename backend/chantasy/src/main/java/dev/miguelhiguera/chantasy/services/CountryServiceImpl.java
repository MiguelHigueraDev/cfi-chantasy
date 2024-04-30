package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
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
    public List<Country> allCountries() {
        List<Country> countries = new ArrayList<>();
        countryRepository.findAll().forEach(countries::add);
        return countries;
    }

    @Override
    public Country createCountry(CountryDto input) {
        Optional<Country> optionalCountry = countryRepository.findByName(input.getName());

        if (optionalCountry.isPresent()) {
            throw new RuntimeException("Country already exists.");
        }

        Country country = new Country();
        country.setName(input.getName());
        country.setCode(input.getCode());
        country.setFlagUrl(input.getFlagUrl());

        return countryRepository.save(country);
    }
}
