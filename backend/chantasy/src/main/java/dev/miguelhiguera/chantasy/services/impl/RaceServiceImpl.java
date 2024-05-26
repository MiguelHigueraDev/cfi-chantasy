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
import dev.miguelhiguera.chantasy.repositories.predictions.ResultRepository;
import dev.miguelhiguera.chantasy.services.RaceService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static dev.miguelhiguera.chantasy.utils.DateTimeUtils.dateStringToLocalDateTime;

@Service
public class RaceServiceImpl implements RaceService {
    private final RaceRepository raceRepository;
    private final CircuitRepository circuitRepository;
    private final QuestionRepository questionRepository;
    private final DriverRepository driverRepository;
    private final ResultRepository resultRepository;

    public RaceServiceImpl(RaceRepository raceRepository, CircuitRepository circuitRepository, QuestionRepository questionRepository, DriverRepository driverRepository, ResultRepository resultRepository) {
        this.raceRepository = raceRepository;
        this.circuitRepository = circuitRepository;
        this.questionRepository = questionRepository;
        this.driverRepository = driverRepository;
        this.resultRepository = resultRepository;
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
    public Optional<Race> getNextRace() throws EntityNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        return raceRepository.findFirstByPredictionEndDateGreaterThanOrderByPredictionEndDateAsc(now);
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

        if (input.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty.");
        }

        if (input.getDriverIds().isEmpty()) {
            throw new IllegalArgumentException("Drivers cannot be empty.");
        }

        LocalDateTime startDate = dateStringToLocalDateTime(input.getPredictionStartDate());
        LocalDateTime endDate = dateStringToLocalDateTime(input.getPredictionEndDate());
        validateDates(startDate, endDate);

        LocalDateTime date = dateStringToLocalDateTime(input.getDate());

        Circuit circuit = getCircuit(input.getCircuitId());

        Race race = Race.builder()
                .name(input.getName())
                .circuit(circuit)
                .maxDnfAwarded(input.getMaxDnfAwarded())
                .date(date)
                .positionPredictionRangeStart(input.getPositionPredictionRangeStart())
                .positionPredictionRangeEnd(input.getPositionPredictionRangeEnd())
                .dnfPoints(input.getDnfPoints())
                .positionPoints(input.getPositionPoints())
                .predictionStartDate(startDate)
                .predictionEndDate(endDate)
                .maxFreePredictions(input.getMaxFreePredictions())
                .isQualifier(input.isQualifier())
                .questions(new HashSet<>())
                .drivers(new HashSet<>()).build();

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
        raceRepository.save(race);

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

        LocalDateTime startDate = dateStringToLocalDateTime(input.getPredictionStartDate());
        LocalDateTime endDate = dateStringToLocalDateTime(input.getPredictionEndDate());
        validateDates(startDate, endDate);

        LocalDateTime date = dateStringToLocalDateTime(input.getDate());

        Race race = optionalRace.get();
        race.setName(input.getName());
        race.setCircuit(circuit);
        race.setMaxDnfAwarded(input.getMaxDnfAwarded());
        race.setDate(date);
        race.setPositionPredictionRangeStart(input.getPositionPredictionRangeStart());
        race.setPositionPredictionRangeEnd(input.getPositionPredictionRangeEnd());
        race.setDnfPoints(input.getDnfPoints());
        race.setPositionPoints(input.getPositionPoints());
        race.setPredictionStartDate(startDate);
        race.setPredictionEndDate(endDate);
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

    @Transactional
    @Override
    public void addRaceResults(Long raceId, List<ResultDto> results) throws EntityNotFoundException {
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Results list cannot be empty.");
        }

        Race race = raceRepository.findById(raceId)
                .orElseThrow(() -> new EntityNotFoundException("Race not found."));
        resultRepository.deleteAllByRaceId(race.getId());

        // Keep track of processed drivers and positions to check for duplicates
        Set<Long> processedDrivers = new HashSet<>();
        Set<Short> processedPositions = new HashSet<>();
        int numResults = results.size();

        for (ResultDto resultDto : results) {
            // Check for duplicate driver
            if (!processedDrivers.add(resultDto.getDriverId())) {
                throw new IllegalArgumentException("Duplicate driver entry for driver ID: " + resultDto.getDriverId());
            }

            // Check for valid and unique position
            short position = resultDto.getPosition();
            if (position < 1 || position > numResults) {
                throw new IllegalArgumentException("Invalid position " + position + ". Position must be between 1 and " + numResults);
            }
            if (!processedPositions.add(position)) {
                throw new IllegalArgumentException("Duplicate position entry for position: " + position);
            }

            Driver driver = driverRepository.findById(resultDto.getDriverId())
                    .orElseThrow(() -> new EntityNotFoundException("Driver not found."));

            Result result = new Result();
            result.setRace(race);
            result.setDriver(driver);
            result.setPosition(position);
            result.setDidFinish(resultDto.getDidFinish());

            resultRepository.save(result);
            race.getResults().add(result);
        }

        // Ensure all positions from 1 to numResults are used
        for (short i = 1; i <= numResults; i++) {
            if (!processedPositions.contains(i)) {
                throw new IllegalArgumentException("Missing position " + i + ". All positions from 1 to " + numResults + " must be included.");
            }
        }
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

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();

        if (endDate.isBefore(now)) {
            throw new IllegalArgumentException("Prediction end date must be in the future.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Prediction end date must be after start date.");
        }
    }

}
