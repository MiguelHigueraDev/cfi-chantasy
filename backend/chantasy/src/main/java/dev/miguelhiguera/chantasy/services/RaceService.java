package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.predictions.Question;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface RaceService {
    Optional<Race> getRace(Long id) throws EntityNotFoundException;

    Optional<Race> getNextRace() throws EntityNotFoundException;

    List<Race> allRaces();

    Race createRace(RaceDto input) throws EntityExistsException, EntityNotFoundException;

    Race updateRace(Long id, RaceDto input) throws EntityNotFoundException;

    void deleteRace(Long id) throws EntityNotFoundException;

    void addRaceResults(Long id, List<ResultDto> results) throws EntityNotFoundException;

    List<Question> getQuestionsForRace(Long raceId) throws EntityNotFoundException;
}