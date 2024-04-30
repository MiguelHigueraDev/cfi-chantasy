package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CircuitService {
    Optional<Circuit> getCircuit(Long id) throws EntityNotFoundException;
    List<Circuit> allCircuits();
    Circuit createCircuit(CircuitDto input) throws EntityExistsException, EntityNotFoundException;
    Circuit updateCircuit(Long id, CircuitDto input) throws EntityNotFoundException;
    void deleteCircuit(Long id) throws EntityNotFoundException;
}
