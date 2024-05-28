package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
