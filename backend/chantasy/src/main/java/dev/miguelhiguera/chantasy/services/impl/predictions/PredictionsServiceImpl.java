package dev.miguelhiguera.chantasy.services.impl.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswerDto;
import dev.miguelhiguera.chantasy.dtos.predictions.FreePredictionDto;
import dev.miguelhiguera.chantasy.dtos.predictions.PredictionsDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.*;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.AnswerRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.FreePredictionRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.QuestionRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.ResultPredictionRepository;
import dev.miguelhiguera.chantasy.services.predictions.PredictionsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PredictionsServiceImpl implements PredictionsService {

    private final RaceRepository raceRepository;
    private final DriverRepository driverRepository;
    private final ResultPredictionRepository resultPredictionRepository;
    private final AnswerRepository answerRepository;
    private final FreePredictionRepository freePredictionRepository;
    private final QuestionRepository questionRepository;

    public PredictionsServiceImpl(RaceRepository raceRepository, DriverRepository driverRepository,
                                  ResultPredictionRepository resultPredictionRepository, AnswerRepository answerRepository, FreePredictionRepository freePredictionRepository, QuestionRepository questionRepository) {
        this.raceRepository = raceRepository;
        this.driverRepository = driverRepository;
        this.resultPredictionRepository = resultPredictionRepository;
        this.answerRepository = answerRepository;
        this.freePredictionRepository = freePredictionRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    @Override
    public void submitPrediction(PredictionsDto predictionsDto, Long raceId, User user) throws EntityNotFoundException {
        List<ResultDto> results = predictionsDto.getResults();
        List<AnswerDto> answers = predictionsDto.getAnswers();
        List<FreePredictionDto> freePredictions = predictionsDto.getFreePredictions();

        Race race = raceRepository.findById(predictionsDto.getRaceId())
                .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));

        if (race.getPredictionEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden hacer predicciones para una carrera que ya ha empezado");
        }

        if (race.getPredictionStartDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden hacer predicciones para una carrera que aún no ha empezado");
        }

        // Remove all current predictions before adding new ones
        clearPredictions(race, user);

        createResults(results, race, user);
        createAnswers(answers, race, user);
        createFreePredictions(freePredictions, race, user);
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
        Set<Question> questions = race.getQuestions();

        if (questions.size() != answers.size()) {
            throw new IllegalArgumentException("El número de respuestas no coincide con el número de preguntas");
        }

        // Convert for fast lookup
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, question -> question));

        for (AnswerDto answerDto: answers) {
            Question question = questionMap.get(answerDto.getQuestionId());

            if (question == null) {
                throw new IllegalArgumentException("ID de pregunta inválido en respuesta: " + answerDto.getQuestionId());
            }

            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setUser(user);
            answer.setAnswer(answerDto.getAnswer());

            answerRepository.save(answer);
        }
    }

    private void createFreePredictions(List<FreePredictionDto> freePredictions, Race race, User user) {
        if (freePredictions.size() > race.getMaxFreePredictions()) {
            throw new IllegalArgumentException("El número de predicciones libres supera el máximo permitido");
        }

        for (FreePredictionDto freePredictionDto: freePredictions) {
            FreePrediction freePrediction = new FreePrediction();
            freePrediction.setPrediction(freePredictionDto.getPrediction());
            freePrediction.setRace(race);
            freePrediction.setUser(user);

            freePredictionRepository.save(freePrediction);
        }
    }

    private void clearPredictions(Race race, User user) {
        freePredictionRepository.deleteByRaceAndUser(race, user);
        resultPredictionRepository.deleteByRaceAndUser(race, user);


        List<Question> questions = questionRepository.findAllByRaceId(race.getId());
        answerRepository.deleteByUserIdAndQuestionIn(user.getId(), questions);
    }
}
