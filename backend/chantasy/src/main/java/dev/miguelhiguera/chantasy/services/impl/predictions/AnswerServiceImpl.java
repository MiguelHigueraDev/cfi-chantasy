package dev.miguelhiguera.chantasy.services.impl.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswerDto;
import dev.miguelhiguera.chantasy.dtos.predictions.AnswersListDto;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import dev.miguelhiguera.chantasy.repositories.UserRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.AnswerRepository;
import dev.miguelhiguera.chantasy.services.predictions.AnswerService;
import dev.miguelhiguera.chantasy.services.predictions.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public AnswerServiceImpl(QuestionService questionService, AnswerRepository answerRepository, UserRepository userRepository) {
        this.questionService = questionService;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Answer> getAnswersForQuestion(Long questionId) throws EntityNotFoundException {
        return answerRepository.findAllByQuestionId(questionId);
    }

    @Override
    public void submitAnswers(AnswersListDto input, Long raceId, Long userId) throws EntityNotFoundException {
        for (AnswerDto answer : input.getAnswers()) {
            Optional<Question> questionOptional = questionService.getQuestion(answer.getQuestionId());
            if (questionOptional.isEmpty()) {
                throw new EntityNotFoundException("Question not found");
            }

            if (!Objects.equals(questionOptional.get().getRace().getId(), raceId)) {
                throw new EntityNotFoundException("Question does not belong to specified race.");
            }

            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

            Answer answerEntity = new Answer();
            answerEntity.setQuestion(questionOptional.get());
            answerEntity.setAnswer(answer.getAnswer());
            answerEntity.setUser(user);

            answerRepository.save(answerEntity);
            questionOptional.get().getAnswers().add(answerEntity);
        }
    }
}
