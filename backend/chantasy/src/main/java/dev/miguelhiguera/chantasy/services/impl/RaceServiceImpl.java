package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.repositories.RaceRepository;
import dev.miguelhiguera.chantasy.services.RaceService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceServiceImpl implements RaceService {
    private final RaceRepository raceRepository;
    private final CountryRepository countryRepository;

    public RaceServiceImpl(RaceRepository raceRepository, CountryRepository countryRepository) {
        this.raceRepository = raceRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public Optional<Race> getRace(Long id) throws EntityNotFoundException {
        return Optional.empty();
    }

    @Override
    public List<Race> allRaces() {
        return List.of();
    }

    @Override
    public Race createRace(RaceDto input) throws EntityExistsException, EntityNotFoundException {
        return null;
    }

    @Override
    public Race updateRace(Long id, RaceDto input) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void deleteRace(Long id) throws EntityNotFoundException {

    }
}
