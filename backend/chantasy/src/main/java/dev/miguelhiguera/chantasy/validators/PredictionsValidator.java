package dev.miguelhiguera.chantasy.validators;

import dev.miguelhiguera.chantasy.dtos.predictions.DnfDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates the predictions entered by users
 */

public class PredictionsValidator {

    public boolean validateDnfPredictions(List<DnfDto> dnfs, Long raceId) {
        // Remove duplicate entries
        Set<DnfDto> predictions = new HashSet<>(dnfs);

        // Get all race predictions


        // Check if all predictions are valid
        for (DnfDto prediction : predictions) {
            if (prediction.getDriverId() != raceId) {
                return false;
            }
        }

        return true;
    }
}
