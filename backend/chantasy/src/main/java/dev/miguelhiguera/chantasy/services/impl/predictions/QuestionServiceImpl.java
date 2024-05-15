package dev.miguelhiguera.chantasy.services.impl.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.QuestionRepository;
import dev.miguelhiguera.chantasy.services.predictions.QuestionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final RaceRepository raceRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, RaceRepository raceRepository) {
        this.questionRepository = questionRepository;
        this.raceRepository = raceRepository;
    }

    @Override
    public Optional<Question> getQuestion(Long id) throws EntityNotFoundException {
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            throw new EntityNotFoundException("Question not found.");
        }

        return optionalQuestion;
    }

    @Override
    public List<Question> allQuestionsForRace(Long raceId) {
        return questionRepository.findAllByRaceId(raceId);
    }

    @Override
    public Question createQuestion(QuestionDto input, Long raceId) throws EntityExistsException, EntityNotFoundException {
        Optional<Question> optionalQuestion = questionRepository.findByRaceIdAndQuestion(raceId, input.getQuestion());

        Optional<Race> optionalRace = raceRepository.findById(raceId);

        if (optionalRace.isEmpty()) {
            throw new EntityNotFoundException("Race not found.");
        }

        if (optionalQuestion.isPresent()) {
            throw new EntityExistsException("Question already exists.");
        }

        Question question = new Question();
        question.setQuestion(input.getQuestion());
        question.setRace(optionalRace.get());
        question.setPoints(input.getPoints());
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Long id, QuestionDto input) throws EntityNotFoundException {
        Optional<Question> optionalQuestion = questionRepository.findById(id);

        if (optionalQuestion.isEmpty()) {
            throw new EntityNotFoundException("Question not found.");
        }

        Question question = optionalQuestion.get();
        question.setQuestion(input.getQuestion());
        question.setPoints(input.getPoints());
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long id) throws EntityNotFoundException {
        // Unimplemented.
    }
}
