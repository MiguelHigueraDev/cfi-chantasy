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

@RequestMapping("/api/questions")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        Optional<Question> optionalQuestion = questionService.getQuestion(id);

        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalQuestion.get());
    }

    @GetMapping("/race/{raceId}")
    public ResponseEntity<List<Question>> allQuestions(@PathVariable Long raceId) {
        return ResponseEntity.ok(questionService.allQuestionsForRace(raceId));
    }

    @PostMapping("/race/{raceId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long raceId, @Valid @RequestBody QuestionDto input) {
        return ResponseEntity.ok(questionService.createQuestion(input, raceId));
    }

    @PutMapping("/question/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDto input) {
        return ResponseEntity.ok(questionService.updateQuestion(id, input));
    }
}
