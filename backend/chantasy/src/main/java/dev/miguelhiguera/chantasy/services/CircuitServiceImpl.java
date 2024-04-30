package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public Optional<Circuit> getCircuit(Long id) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(id);

        if (optionalCircuit.isEmpty() || optionalCircuit.get().isDeleted()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        return optionalCircuit;
    }

    @Override
    public List<Circuit> allCircuits() {
        List<Circuit> circuits = new ArrayList<>();
        circuitRepository.findAll().forEach(circuit -> {
            if (!circuit.isDeleted()) {
                circuits.add(circuit);
            }
        });
        return circuits;
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
