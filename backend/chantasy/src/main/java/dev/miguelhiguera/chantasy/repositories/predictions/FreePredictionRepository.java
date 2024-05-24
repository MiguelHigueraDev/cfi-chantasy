package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.FreePrediction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FreePredictionRepository extends CrudRepository<FreePrediction, Long> {

    @Transactional
    void deleteByRaceAndUser(Race race, User user);
}
