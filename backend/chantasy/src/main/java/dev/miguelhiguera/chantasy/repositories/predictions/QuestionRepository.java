package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.predictions.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findAllByRaceId(Long raceId);
    Optional<Question> findByRaceIdAndQuestion(Long raceId, String name);
}
