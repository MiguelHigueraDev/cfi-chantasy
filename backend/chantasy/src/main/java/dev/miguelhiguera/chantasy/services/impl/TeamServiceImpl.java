package dev.miguelhiguera.chantasy.services.impl;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import dev.miguelhiguera.chantasy.services.TeamService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Optional<Team> getTeam(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isEmpty() || optionalTeam.get().isDeleted()) {
            throw new EntityNotFoundException("Team not found.");
        }

        return optionalTeam;
    }

    @Override
    public Page<Team> allTeams(Pageable pageable) {
        return teamRepository.findAll(pageable)
                .map(team -> {
                    if (team.isDeleted()) {
                        return null;
                    }
                    return team;
                });
    }

    @Override
    public Team createTeam(@Validated TeamDto input) {
        Optional<Team> optionalTeam = teamRepository.findByName(input.getName());

        if (optionalTeam.isPresent()) {
            throw new EntityExistsException("Team already exists.");
        }

        Team team = new Team();
        team.setName(input.getName());
        team.setLogoUrl(input.getLogoUrl());
        team.setDeleted(false);

        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long id, TeamDto input) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isEmpty() || optionalTeam.get().isDeleted()) {
            throw new EntityNotFoundException("Team not found.");
        }

        Team team = optionalTeam.get();
        team.setName(input.getName());
        team.setLogoUrl(input.getLogoUrl());

        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalTeam.isEmpty() || optionalTeam.get().isDeleted()) {
            throw new EntityNotFoundException("Team not found.");
        }

        Team team = optionalTeam.get();
        team.setDeleted(true);
        teamRepository.save(team);
    }

}
