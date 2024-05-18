package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long>{
    List<Answer> findAllByQuestionId(Long questionId);

    @Modifying
    @Query("DELETE FROM Answer a WHERE a.user.id = :userId AND a.question IN :questions")
    void deleteByUserIdAndQuestionIn(Long userId, List<Question> questions);
}
