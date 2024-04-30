package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.services.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequestMapping("/api/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        Optional<Team> optionalTeam = teamService.getTeam(id);

        if (optionalTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalTeam.get());
    }

    @GetMapping("/")
    public ResponseEntity<List<Team>> allTeams() {
        return ResponseEntity.ok(teamService.allTeams());
    }

    @PostMapping("/")
    public ResponseEntity<Team> createTeam(@Valid @RequestBody TeamDto input) {
        return ResponseEntity.ok(teamService.createTeam(input));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamDto input) {
        return ResponseEntity.ok(teamService.updateTeam(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
