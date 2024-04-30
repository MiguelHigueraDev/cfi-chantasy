package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
