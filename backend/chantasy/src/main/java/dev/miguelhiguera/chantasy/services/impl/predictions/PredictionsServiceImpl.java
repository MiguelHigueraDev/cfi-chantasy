package dev.miguelhiguera.chantasy.services.impl.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswerDto;
import dev.miguelhiguera.chantasy.dtos.predictions.FreePredictionDto;
import dev.miguelhiguera.chantasy.dtos.predictions.PredictionsDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import dev.miguelhiguera.chantasy.entities.predictions.Result;
import dev.miguelhiguera.chantasy.entities.predictions.ResultPrediction;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.ResultPredictionRepository;
import dev.miguelhiguera.chantasy.services.predictions.PredictionsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PredictionsServiceImpl implements PredictionsService {

    private final RaceRepository raceRepository;
    private final DriverRepository driverRepository;
    private final ResultPredictionRepository resultPredictionRepository;

    public PredictionsServiceImpl(RaceRepository raceRepository, DriverRepository driverRepository
            , ResultPredictionRepository resultPredictionRepository) {
        this.raceRepository = raceRepository;
        this.driverRepository = driverRepository;
        this.resultPredictionRepository = resultPredictionRepository;

    }

    @Override
    public void submitPrediction(PredictionsDto predictionsDto, Long raceId, User user) throws EntityNotFoundException {
        List<ResultDto> results = predictionsDto.getResults();
        List<AnswerDto> answers = predictionsDto.getAnswers();
        List<FreePredictionDto> freePredictions = predictionsDto.getFreePredictions();

        Race race = raceRepository.findById(predictionsDto.getRaceId())
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));

        createResults(results, race, user);

    }

    @Override
    public void updatePrediction(PredictionsDto predictionsDto, Long raceId, User user) throws EntityNotFoundException {

    }

    private void createResults(List<ResultDto> results, Race race, User user) {
        // Check that results are valid for the current race
        Set<Result> raceResults = race.getResults();

        // Keep track of processed results to check for duplicates
        Set<Long> processedResults = new HashSet<>();
        Set<Short> processedPositions = new HashSet<>();
        for (ResultDto resultDto: results) {
            // Check for duplicate result
            if (!processedResults.add(resultDto.getDriverId())) {
                throw new IllegalArgumentException("No se pueden añadir resultados duplicados");
            }

            short position = resultDto.getPosition();
            if (position < 1 || position > 20) {
                throw new IllegalArgumentException("La posición debe estar entre 1 y 20");
            }

            if (!processedPositions.add(position)) {
                throw new IllegalArgumentException("No se pueden añadir posiciones duplicadas");
            }

            Driver driver = driverRepository.findById(resultDto.getDriverId())
                    .orElseThrow(() -> new EntityNotFoundException("Piloto no encontrado"));

            ResultPrediction resultPrediction = new ResultPrediction();
            resultPrediction.setRace(race);
            resultPrediction.setDriver(driver);
            resultPrediction.setPosition(position);
            resultPrediction.setDidFinish(resultDto.getDidFinish());
            resultPrediction.setUser(user);

            resultPredictionRepository.save(resultPrediction);
        }
    }

    private void createAnswers(List<AnswerDto> answers, Race race, User user) {

    }
}
