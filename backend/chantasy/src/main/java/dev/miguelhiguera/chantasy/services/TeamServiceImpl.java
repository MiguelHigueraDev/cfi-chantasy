package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import org.springframework.stereotype.Service;

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
    public List<Team> allTeams() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }

    @Override
    public Team createTeam(TeamDto input) {
        Optional<Team> optionalTeam = teamRepository.findByName(input.getName());

        if (optionalTeam.isPresent()) {
            throw new RuntimeException("Team already exists.");
        }

        Team team = new Team();
        team.setName(input.getName());
        team.setLogoUrl(input.getLogoUrl());
        team.setDeleted(false);

        return teamRepository.save(team);
    }
}
