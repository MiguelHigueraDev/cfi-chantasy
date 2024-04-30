package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Circuit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CircuitRepository extends CrudRepository<Circuit, Long> {
    Optional<Circuit> findByName(String name);
    Optional<List<Circuit>> findAllByCountryId(Long countryId);
    Optional<List<Circuit>> findAllByCountryName(String countryName);
}
