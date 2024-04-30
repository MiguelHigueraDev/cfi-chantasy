package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Team>> allTeams() {
        return ResponseEntity.ok(teamService.allTeams());
    }

    @PostMapping("/")
    public ResponseEntity<Team> createTeam(@RequestBody TeamDto input) {
        return ResponseEntity.ok(teamService.createTeam(input));
    }


}
