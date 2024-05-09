package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.predictions.Dnf;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DnfRepository extends CrudRepository<Dnf, Long>{
    List<Dnf> findAllByRaceId(Long raceId);
}
