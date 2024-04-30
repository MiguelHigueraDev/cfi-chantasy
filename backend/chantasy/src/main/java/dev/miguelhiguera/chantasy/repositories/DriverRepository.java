package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByName(String name);
    List<Driver> findByTeamId(Long teamId);
}
