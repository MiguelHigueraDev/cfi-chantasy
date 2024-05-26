package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountrySeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final CountryRepository countryRepository;

    public CountrySeeder(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createCountries();
    }

    private void createCountries() {
        new CountryDto();
        CountryDto countryDto1 = CountryDto.builder().name("Monaco").code("MO").flagUrl("https://test.com/monaco.png").build();
        CountryDto countryDto2 = CountryDto.builder().name("Argentina").code("AR").flagUrl("https://test.com/argentina.png").build();

        new Country();
        Country country1 = Country.builder()
                .name(countryDto1.getName())
                .code(countryDto1.getCode())
                .flagUrl(countryDto1.getFlagUrl()).build();

        Country country2 = Country.builder()
                .name(countryDto2.getName())
                .code(countryDto2.getCode())
                .flagUrl(countryDto2.getFlagUrl()).build();

        countryRepository.saveAll(List.of(country1, country2));
    }
}
