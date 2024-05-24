package dev.miguelhiguera.chantasy.repositories.predictions;

import dev.miguelhiguera.chantasy.entities.Race;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.entities.predictions.ResultPrediction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ResultPredictionRepository extends CrudRepository<ResultPrediction, Long>{

    @Transactional
    void deleteByRaceAndUser(Race race, User user);
}
