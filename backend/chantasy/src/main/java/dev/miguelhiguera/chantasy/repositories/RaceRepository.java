package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RaceRepository extends JpaRepository<Race, Long> {
    Optional<Race> findByName(String name);
    // Finds race with prediction end date greater than current date and orders by prediction end date to get the closest date
    Optional<Race> findFirstByPredictionEndDateGreaterThanOrderByPredictionEndDateAsc(LocalDateTime currentDate);
}
