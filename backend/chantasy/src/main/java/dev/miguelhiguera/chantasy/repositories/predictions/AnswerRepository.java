package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long>{
    List<Answer> findAllByQuestionId(Long questionId);
}
