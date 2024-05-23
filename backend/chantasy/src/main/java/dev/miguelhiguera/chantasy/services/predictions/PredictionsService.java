package dev.miguelhiguera.chantasy.services.predictions;

import dev.miguelhiguera.chantasy.dtos.predictions.PredictionsDto;
import dev.miguelhiguera.chantasy.entities.User;
import jakarta.persistence.EntityNotFoundException;


public interface PredictionsService {
    void submitPrediction(PredictionsDto predictionsDto, Long raceId, User user) throws EntityNotFoundException;
    void updatePrediction(PredictionsDto predictionsDto, Long raceId, User user) throws EntityNotFoundException;
}
