package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;

import java.util.List;

public interface TeamService {
    List<Team> allTeams();
    Team createTeam(TeamDto input);
}
