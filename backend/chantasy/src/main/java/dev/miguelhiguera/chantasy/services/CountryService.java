package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;

import java.util.List;

public interface CountryService {
    List<Country> allCountries();
    Country createCountry(CountryDto input);
}
