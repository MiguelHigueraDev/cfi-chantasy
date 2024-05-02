package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.services.impl.CircuitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CircuitServiceTests {

    @Mock
    private CircuitRepository circuitRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CircuitServiceImpl circuitService;

    private Circuit circuit;
    private Country country;

    @BeforeEach
    public void init() {
        country = Country.builder().name("Mexico").code("MEX").flagUrl("https://flag.com/mexico").build();
        circuit = Circuit.builder().name("Mexico City").country(country).build();
    }

    @Test
    public void CircuitService_CreateCircuit_ReturnsCircuit() {
        when(circuitRepository.save(Mockito.any(Circuit.class))).thenReturn(circuit);

        Circuit savedCircuit = circuitService.createCircuit(circuitDto);

        assertThat(savedCircuit).isNotNull();
    }

}
