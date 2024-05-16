package dev.miguelhiguera.chantasy.validators;

import dev.miguelhiguera.chantasy.annotations.ValidDateRange;
import dev.miguelhiguera.chantasy.dtos.RaceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, RaceDto> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public boolean isValid(RaceDto raceDto, ConstraintValidatorContext context) {
        try {
            LocalDateTime startDate = LocalDateTime.parse(raceDto.getPredictionStartDate(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(raceDto.getPredictionEndDate(), formatter);
            return startDate.isBefore(endDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
