package dev.miguelhiguera.chantasy.services.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Optional<Question> getQuestion(Long id) throws EntityNotFoundException;
    List<Question> allQuestionsForRace(Long raceId);
    Question createQuestion(QuestionDto input, Long raceId) throws EntityExistsException, EntityNotFoundException;
    Question updateQuestion(Long id, QuestionDto input) throws EntityNotFoundException;
    void deleteQuestion(Long id) throws EntityNotFoundException;
}
