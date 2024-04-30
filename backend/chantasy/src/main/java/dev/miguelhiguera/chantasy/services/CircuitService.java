package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.CircuitDto;
import dev.miguelhiguera.chantasy.entities.Circuit;

import java.util.List;

public interface CircuitService {
    List<Circuit> allCircuits();
    Circuit createCircuit(CircuitDto input);
}
