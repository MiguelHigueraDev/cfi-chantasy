package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.dtos.TeamDto;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final TeamRepository teamRepository;

    public TeamSeeder(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createTeams();
    }

    private void createTeams() {
        TeamDto teamDto1 = TeamDto.builder()
                .name("Team 1")
                .logoUrl("https://test.com/team1.png")
                .build();

        TeamDto teamDto2 = TeamDto.builder()
                .name("Team 2")
                .logoUrl("https://test.com/team2.png")
                .build();

        Team team1 = Team.builder()
                .name(teamDto1.getName())
                .logoUrl(teamDto1.getLogoUrl())
                .build();

        Team team2 = Team.builder()
                .name(teamDto2.getName())
                .logoUrl(teamDto2.getLogoUrl())
                .build();

        teamRepository.saveAll(List.of(team1, team2));
    }
}
