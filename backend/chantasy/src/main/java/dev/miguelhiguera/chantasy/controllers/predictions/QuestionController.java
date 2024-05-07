package dev.miguelhiguera.chantasy.controllers.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import dev.miguelhiguera.chantasy.services.predictions.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        Optional<Question> optionalQuestion = questionService.getQuestion(id);

        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalQuestion.get());
    }

    @GetMapping("/{raceId}")
    public ResponseEntity<List<Question>> allQuestions(@PathVariable Long raceId) {
        return ResponseEntity.ok(questionService.allQuestionsForRace(raceId));
    }

    @PostMapping("/{raceId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long raceId, @Valid @RequestBody QuestionDto input) {
        return ResponseEntity.ok(questionService.createQuestion(input, raceId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDto input) {
        return ResponseEntity.ok(questionService.updateQuestion(id, input));
    }
}
