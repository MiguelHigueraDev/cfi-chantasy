package dev.miguelhiguera.chantasy.validators;

import dev.miguelhiguera.chantasy.annotations.ValidPositionRange;
import dev.miguelhiguera.chantasy.dtos.RaceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositionRangeValidator implements ConstraintValidator<ValidPositionRange, RaceDto> {

    @Override
    public void initialize(ValidPositionRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(RaceDto raceDto, ConstraintValidatorContext context) {
        return raceDto.getPositionPredictionRangeStart() < raceDto.getPositionPredictionRangeEnd();
    }
}
