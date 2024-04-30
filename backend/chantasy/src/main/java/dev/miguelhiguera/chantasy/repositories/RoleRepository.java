package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Role;
import dev.miguelhiguera.chantasy.entities.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
