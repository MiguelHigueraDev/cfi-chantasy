package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {
    Optional<Circuit> findByName(String name);
    Optional<List<Circuit>> findAllByCountryId(Long countryId);
    Optional<List<Circuit>> findAllByCountryName(String countryName);
}
