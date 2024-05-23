package dev.miguelhiguera.chantasy.controllers.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.PredictionsDto;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.services.predictions.AnswerService;
import dev.miguelhiguera.chantasy.services.predictions.PredictionsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Handles the predictions entered by users
 */

@RequestMapping("/api/predictions")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
@RestController
public class PredictionsController {

    private final PredictionsService predictionsService;

    public PredictionsController(PredictionsService predictionsService) {
        this.predictionsService = predictionsService;
    }

    @PostMapping("/submit/{raceId}")
    public ResponseEntity<Void> submitPredictions(@PathVariable Long raceId, @Valid @RequestBody PredictionsDto predictionsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        predictionsService.submitPrediction(predictionsDto, raceId, principal);
        return ResponseEntity.noContent().build();
    }


}
