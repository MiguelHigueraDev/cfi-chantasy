package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import dev.miguelhiguera.chantasy.entities.predictions.Result;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.QuestionRepository;
import dev.miguelhiguera.chantasy.services.RaceService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static dev.miguelhiguera.chantasy.utils.DateTimeUtils.dateStringToLocalDateTime;

@Service
public class RaceServiceImpl implements RaceService {
    private final RaceRepository raceRepository;
    private final CircuitRepository circuitRepository;
    private final QuestionRepository questionRepository;
    private final DriverRepository driverRepository;

    public RaceServiceImpl(RaceRepository raceRepository, CircuitRepository circuitRepository, QuestionRepository questionRepository, DriverRepository driverRepository) {
        this.raceRepository = raceRepository;
        this.circuitRepository = circuitRepository;
        this.questionRepository = questionRepository;
        this.driverRepository = driverRepository;
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
                .isQualifier(input.isQualifier())
                .questions(new HashSet<>())
                .drivers(new HashSet<>()).build();

        if (input.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty.");
        }

        if (input.getDriverIds().isEmpty()) {
            throw new IllegalArgumentException("Drivers cannot be empty.");
        }

        raceRepository.save(race);

        for (QuestionDto dto : input.getQuestions()) {
            Question question = new Question();
            question.setQuestion(dto.getQuestion());
            question.setPoints(dto.getPoints());
            question.setRace(race);
            questionRepository.save(question);
            race.getQuestions().add(question);
        }

        addDrivers(race, input);

        return race;
    }

    @Transactional
    @Override
    public Race updateRace(Long id, RaceDto input) throws EntityNotFoundException {
        Optional<Race> optionalRace = raceRepository.findById(id);
        Circuit circuit = getCircuit(input.getCircuitId());

        if (optionalRace.isEmpty() || optionalRace.get().isDeleted()) {
            throw new EntityNotFoundException("Race not found.");
        }

        if (input.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty.");
        }

        if (input.getDriverIds().isEmpty()) {
            throw new IllegalArgumentException("Drivers cannot be empty.");
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

        List<Question> newQuestions = new ArrayList<>();
        for (QuestionDto dto : input.getQuestions()) {
            Question question = new Question();
            question.setQuestion(dto.getQuestion());
            question.setPoints(dto.getPoints());
            question.setRace(race);
            newQuestions.add(question);
        }

        race.getQuestions().clear();
        questionRepository.deleteAllByRace(race);
        for (Question question : newQuestions) {
            questionRepository.save(question);
            race.getQuestions().add(question);
        }

        race.getDrivers().clear();
        addDrivers(race, input);

        race.getResults().clear();

        raceRepository.save(race);

        return race;
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

    @Override
    public Race addRaceResults(Race race, List<ResultDto> results) throws EntityNotFoundException {

        for (ResultDto resultDto : results) {
            Driver driver = driverRepository.findById(resultDto.getDriverId()).orElseThrow(() -> new EntityNotFoundException("Driver not found."));
            Result result = new Result();
            result.setRace(race);
            result.setDriver(driver);
            result.setPosition(resultDto.getPosition());
            result.
        }
        // TODO: Implement this method
        return null;
    }

    @Override
    public List<Question> getQuestionsForRace(Long raceId) throws EntityNotFoundException {
        return questionRepository.findAllByRaceId(raceId);
    }

    private Circuit getCircuit(Long circuitId) throws EntityNotFoundException {
        Optional<Circuit> optionalCircuit = circuitRepository.findById(circuitId);

        if (optionalCircuit.isEmpty() || optionalCircuit.get().isDeleted()) {
            throw new EntityNotFoundException("Circuit not found.");
        }

        return optionalCircuit.get();
    }

    private void addDrivers(Race race, RaceDto input) {
        for (Long driverId: input.getDriverIds()) {
            Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException("Driver not found."));
            race.getDrivers().add(driver);
        }
    }

    private void clearRaceData(Race race) {

    }
}
