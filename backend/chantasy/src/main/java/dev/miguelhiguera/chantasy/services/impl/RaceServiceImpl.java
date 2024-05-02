package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.services.RaceService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.miguelhiguera.chantasy.utils.DateTimeUtils.dateStringToLocalDateTime;

@Service
public class RaceServiceImpl implements RaceService {
    private final RaceRepository raceRepository;
    private final CircuitRepository circuitRepository;

    public RaceServiceImpl(RaceRepository raceRepository, CircuitRepository circuitRepository) {
        this.raceRepository = raceRepository;
        this.circuitRepository = circuitRepository;
    }


    @Override
    public Optional<Race> getRace(Long id) throws EntityNotFoundException {
        Optional<Race> optionalRace = raceRepository.findById(id);

        if (optionalRace.isEmpty() || optionalRace.get().isDeleted()) {
            throw new EntityNotFoundException("Race not found.");
        }

        return optionalRace;
    }

    @Override
    public List<Race> allRaces() {
        List<Race> races = new ArrayList<>();
        raceRepository.findAll().forEach(race -> {
            if (!race.isDeleted()) {
                races.add(race);
            }
        });
        return races;
    }

    @Override
    public Race createRace(RaceDto input) throws EntityExistsException, EntityNotFoundException {
        Circuit circuit = getCircuit(input.getCircuitId());

        Race race = Race.builder()
                .name(input.getName())
                .circuit(circuit)
                .maxDnfAwarded(input.getMaxDnfAwarded())
                .positionPredictionRangeStart(input.getPositionPredictionRangeStart())
                .positionPredictionRangeEnd(input.getPositionPredictionRangeEnd())
                .dnfPoints(input.getDnfPoints())
                .positionPoints(input.getPositionPoints())
                .predictionStartDate(dateStringToLocalDateTime(input.getPredictionStartDate()))
                .predictionEndDate(dateStringToLocalDateTime(input.getPredictionEndDate()))
                .maxFreePredictions(input.getMaxFreePredictions())
                .isQualifier(input.isQualifier()).build();

        return raceRepository.save(race);
    }

    @Override
    public Race updateRace(Long id, RaceDto input) throws EntityNotFoundException {
        Optional<Race> optionalRace = raceRepository.findById(id);
        Circuit circuit = getCircuit(input.getCircuitId());

        if (optionalRace.isEmpty() || optionalRace.get().isDeleted()) {
            throw new EntityNotFoundException("Race not found.");
        }

        Race race = optionalRace.get();
        race.setName(input.getName());
        race.setCircuit(circuit);
        race.setMaxDnfAwarded(input.getMaxDnfAwarded());
        race.setPositionPredictionRangeStart(input.getPositionPredictionRangeStart());
        race.setPositionPredictionRangeEnd(input.getPositionPredictionRangeEnd());
        race.setDnfPoints(input.getDnfPoints());
        race.setPositionPoints(input.getPositionPoints());
        race.setPredictionStartDate(dateStringToLocalDateTime(input.getPredictionStartDate()));
        race.setPredictionEndDate(dateStringToLocalDateTime(input.getPredictionEndDate()));
        race.setMaxFreePredictions(input.getMaxFreePredictions());
        race.setQualifier(input.isQualifier());

        return raceRepository.save(race);
    }

    @Override
    public void deleteRace(Long id) throws EntityNotFoundException {
        Optional<Race> optionalRace = raceRepository.findById(id);

        if (optionalRace.isEmpty()) {
            throw new EntityNotFoundException("Race not found.");
        }

        Race race = optionalRace.get();
        race.setDeleted(true);
        raceRepository.save(race);
    }

    private Circuit getCircuit(Long circuitId) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(circuitId);

        if (optionalCircuit.isEmpty() || optionalCircuit.get().isDeleted()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        return optionalCircuit.get();
    }
}
