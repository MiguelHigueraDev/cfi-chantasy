package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CircuitServiceImpl implements CircuitService {
    private final CircuitRepository circuitRepository;
    private final CountryRepository countryRepository;

    public CircuitServiceImpl(CircuitRepository circuitRepository, CountryRepository countryRepository) {
        this.circuitRepository = circuitRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public List<Circuit> allCircuits() {
        List<Circuit> circuits = new ArrayList<>();
        circuitRepository.findAll().forEach(circuits::add);
        return circuits;
    }

    @Override
    public Circuit createCircuit(CircuitDto input) {
        Optional<Circuit> optionalCircuit = circuitRepository.findByName(input.getName());
        Optional<Country> optionalCountry = countryRepository.findById(input.getCountryId());

        if (optionalCountry.isEmpty()) {
            throw new RuntimeException("Invalid country ID.");
        }

        if (optionalCircuit.isPresent()) {
            throw new RuntimeException("Circuit already exists.");
        }

        Circuit circuit = new Circuit();
        circuit.setName(input.getName());
        circuit.setCountry(optionalCountry.get());

        return circuitRepository.save(circuit);
    }
}
