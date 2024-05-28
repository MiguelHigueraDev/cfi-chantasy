package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.services.CircuitService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Optional<Circuit> getCircuit(Long id) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(id);

        if (optionalCircuit.isEmpty() || optionalCircuit.get().isDeleted()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        return optionalCircuit;
    }

    @Override
    public Page<Circuit> allCircuits(Pageable pageable) {
        return circuitRepository.findAll(pageable)
                .map(circuit -> {
                    if (circuit.isDeleted()) {
                        return null;
                    }
                    return circuit;
                });
    }

    @Override
    public Circuit createCircuit(CircuitDto input) {
        Optional<Circuit> optionalCircuit = circuitRepository.findByName(input.getName());
        Country country = getCountry(input.getCountryId());

        if (optionalCircuit.isPresent()) {
            throw new EntityExistsException("Circuit already exists.");
        }

        Circuit circuit = new Circuit();
        circuit.setName(input.getName());
        circuit.setCountry(country);

        return circuitRepository.save(circuit);
    }

    @Override
    public Circuit updateCircuit(Long id, CircuitDto input) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(id);
        Country country = getCountry(input.getCountryId());

        if (optionalCircuit.isEmpty()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        Circuit circuit = optionalCircuit.get();
        circuit.setName(input.getName());
        circuit.setCountry(country);

        return circuitRepository.save(circuit);
    }

    @Override
    public void deleteCircuit(Long id) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(id);

        if (optionalCircuit.isEmpty()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        Circuit circuit = optionalCircuit.get();
        circuit.setDeleted(true);
        circuitRepository.save(circuit);
    }

    private Country getCountry(Long countryId) {
        Optional<Country> optionalCountry = countryRepository.findById(countryId);

        if (optionalCountry.isEmpty() || optionalCountry.get().isDeleted()) {
            throw new EntityNotFoundException("Invalid country ID.");
        }

        return optionalCountry.get();
    }
}
