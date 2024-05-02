package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CountryDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.services.impl.CountryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTests {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    public void CountryService_CreateCountry_ReturnsCountryDto() {
        Country country = Country.builder()
                .name("Mexico")
                .code("MEX")
                .flagUrl("https://flag.com/mexico").build();

        CountryDto countryDto = CountryDto.builder()
                .name("Mexico")
                .code("MEX")
                .flagUrl("https://flag.com/mexico").build();

        when(countryRepository.save(Mockito.any(Country.class))).thenReturn(country);

        Country savedCountry = countryService.createCountry(countryDto);

        assertThat(savedCountry).isNotNull();
    }
}
