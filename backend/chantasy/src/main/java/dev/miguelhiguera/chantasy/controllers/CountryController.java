package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")

@RequestMapping("/api/countries")
@RestController
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable Long id) {
        Optional<Country> optionalCountry = countryService.getCountry(id);

        if (optionalCountry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalCountry.get());
    }

    @GetMapping("/")
    public ResponseEntity<List<Country>> allCountries() {
        return ResponseEntity.ok(countryService.allCountries());
    }

    @PostMapping("/")
    public ResponseEntity<Country> createCountry(@Valid @RequestBody CountryDto input) {
        return ResponseEntity.ok(countryService.createCountry(input));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryDto input) {
        return ResponseEntity.ok(countryService.updateCountry(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
