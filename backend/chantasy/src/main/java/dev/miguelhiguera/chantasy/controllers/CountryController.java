package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/countries")
@RestController
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Country>> allCountries() {
        return ResponseEntity.ok(countryService.allCountries());
    }

    @PostMapping("/")
    public ResponseEntity<Country> createCountry(@RequestBody CountryDto input) {
        return ResponseEntity.ok(countryService.createCountry(input));
    }
}
