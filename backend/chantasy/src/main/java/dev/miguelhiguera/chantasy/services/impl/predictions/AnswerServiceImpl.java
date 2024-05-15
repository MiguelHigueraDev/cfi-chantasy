package dev.miguelhiguera.chantasy.services.impl.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswerDto;
import dev.miguelhiguera.chantasy.dtos.predictions.AnswersListDto;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.repositories.UserRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.AnswerRepository;
import dev.miguelhiguera.chantasy.repositories.predictions.QuestionRepository;
import dev.miguelhiguera.chantasy.services.predictions.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final RaceRepository raceRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, UserRepository userRepository, QuestionRepository questionRepository, RaceRepository raceRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.raceRepository = raceRepository;
    }


    @Override
    public List<Answer> getAnswersForQuestion(Long questionId) throws EntityNotFoundException {
        return answerRepository.findAllByQuestionId(questionId);
    }

    /**
     * Submit answers for the race. If the user has already submitted answers, they will be replaced.
     * If the prediction period has ended, an exception will be thrown.
     * @param input
     * @param raceId
     * @param userId
     * @throws EntityNotFoundException
     */
    @Transactional
    @Override
    public void submitAnswers(AnswersListDto input, Long raceId, Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Race race = raceRepository.findById(raceId)
                .orElseThrow(() -> new EntityNotFoundException("Race not found"));

        if (LocalDateTime.now().isAfter(race.getPredictionEndDate())) {
            throw new IllegalStateException("The prediction period for this race has ended.");
        }

        List<Question> questions = questionRepository.findAllByRaceId(raceId);

        if (questions.isEmpty()) {
            throw new EntityNotFoundException("No questions found for the race");
        }

        answerRepository.deleteByUserIdAndQuestionIn(userId, questions);

        for (AnswerDto answerDto : input.getAnswers()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answerDto.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            Answer answerEntity = new Answer();
            answerEntity.setQuestion(question);
            answerEntity.setAnswer(answerDto.getAnswer());
            answerEntity.setUser(user);

            answerRepository.save(answerEntity);
        }
    }
}
