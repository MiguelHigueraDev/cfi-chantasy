package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("countrySeeder")
public class CircuitSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final CountryRepository countryRepository;
    private final CircuitRepository circuitRepository;

    public CircuitSeeder(CountryRepository countryRepository, CircuitRepository circuitRepository) {
        this.countryRepository = countryRepository;
        this.circuitRepository = circuitRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createCircuits();
    }

    private void createCircuits() {
        CircuitDto circuitDto1 = CircuitDto.builder()
                .name("Circuit de Monaco")
                .countryId(1L)
                .build();

        CircuitDto circuitDto2 = CircuitDto.builder()
                .name("Circuit 2")
                .countryId(2L)
                .build();

        Country country1 = countryRepository.findById(circuitDto1.getCountryId()).orElseThrow();
        Country country2 = countryRepository.findById(circuitDto2.getCountryId()).orElseThrow();

        Circuit circuit1 = Circuit.builder()
                .name(circuitDto1.getName())
                .country(country1)
                .build();

        Circuit circuit2 = Circuit.builder()
                .name(circuitDto2.getName())
                .country(country2)
                .build();

        circuitRepository.saveAll(List.of(circuit1, circuit2));
    }
}
