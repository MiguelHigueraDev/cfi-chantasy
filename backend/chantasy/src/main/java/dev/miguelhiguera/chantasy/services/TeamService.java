package dev.miguelhiguera.chantasy.services;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeamService {
    Optional<Team> getTeam(Long id) throws EntityNotFoundException;
    Page<Team> allTeams(Pageable pageable);
    Team createTeam(TeamDto input) throws EntityExistsException;
    Team updateTeam(Long id, TeamDto input) throws EntityNotFoundException;
    void deleteTeam(Long id) throws EntityNotFoundException;
}
