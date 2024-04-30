package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Optional<Country> findByName(String name);
    Optional<Country> findByCode(String code);
}
