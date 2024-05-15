package dev.miguelhiguera.chantasy.services.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswersListDto;
import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface AnswerService {
    List<Answer> getAnswersForQuestion(Long questionId) throws EntityNotFoundException;
    void submitAnswers(AnswersListDto input, Long raceId, Long userId) throws EntityNotFoundException;

}
