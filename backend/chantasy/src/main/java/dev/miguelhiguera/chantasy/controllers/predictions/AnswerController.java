package dev.miguelhiguera.chantasy.controllers.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.AnswersListDto;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.Answer;
import dev.miguelhiguera.chantasy.services.predictions.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/answers")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<List<Answer>> getAnswersForQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswersForQuestion(id));
    }

    @PostMapping("/submit/{raceId}")
    public ResponseEntity<Void> submitAnswers(@PathVariable Long raceId, @Valid @RequestBody AnswersListDto answersListDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        answerService.submitAnswers(answersListDto, raceId, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
