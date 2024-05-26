package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.dtos.DriverDto;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn({"teamSeeder", "countrySeeder"})
public class DriverSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final CountryRepository countryRepository;
    private final TeamRepository teamRepository;
    private final DriverRepository driverRepository;

    public DriverSeeder(CountryRepository countryRepository, TeamRepository teamRepository, DriverRepository driverRepository) {
        this.countryRepository = countryRepository;
        this.teamRepository = teamRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createDrivers();
    }

    private void createDrivers() {
        DriverDto driverDto1 = DriverDto.builder()
                .name("Driver 1")
                .code("D1")
                .countryId(1L)
                .teamId(1L)
                .build();

        DriverDto driverDto2 = DriverDto.builder()
                .name("Driver 2")
                .code("D2")
                .countryId(2L)
                .teamId(2L)
                .build();

        Country country1 = countryRepository.findById(driverDto1.getCountryId()).orElseThrow();
        Country country2 = countryRepository.findById(driverDto2.getCountryId()).orElseThrow();

        Team team1 = teamRepository.findById(driverDto1.getTeamId()).orElseThrow();
        Team team2 = teamRepository.findById(driverDto2.getTeamId()).orElseThrow();

        Driver driver1 = Driver.builder()
                .name(driverDto1.getName())
                .code(driverDto1.getCode())
                .country(country1)
                .team(team1)
                .build();

        Driver driver2 = Driver.builder()
                .name(driverDto2.getName())
                .code(driverDto2.getCode())
                .country(country2)
                .team(team2)
                .build();

        driverRepository.saveAll(List.of(driver1, driver2));
    }
}
