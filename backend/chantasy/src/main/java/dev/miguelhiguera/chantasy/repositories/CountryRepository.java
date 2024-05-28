package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
    Optional<Country> findByCode(String code);
}
