package dev.miguelhiguera.chantasy.bootstrap;

import dev.miguelhiguera.chantasy.dtos.RaceDto;
import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.services.RaceService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@DependsOn({"circuitSeeder", "driverSeeder"})
@Component
public class RaceSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RaceService raceService;

    public RaceSeeder(RaceService raceService) {
        this.raceService = raceService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createRaces();
    }

    private void createRaces() {
        List<QuestionDto> questionDtos = this.createQuestionDtos();
        RaceDto raceDto1 = RaceDto.builder()
                .name("Monaco GP")
                .circuitId(1L)
                .maxDnfAwarded((short) 2)
                .positionPredictionRangeStart((short) 1)
                .positionPredictionRangeEnd((short) 5)
                .dnfPoints((short) 1)
                .positionPoints((short) 1)
                .date("2024-07-15T12:30")
                .predictionStartDate("2024-07-15T12:30")
                .predictionEndDate("2024-07-18T12:30")
                .maxFreePredictions((short) 2)
                .isQualifier(false)
                .driverIds(List.of(1L, 2L))
                .questions(questionDtos.subList(0, 2))
                .build();

        RaceDto raceDto2 = RaceDto.builder()
                .name("Los Angeles GP")
                .circuitId(2L)
                .maxDnfAwarded((short) 2)
                .positionPredictionRangeStart((short) 1)
                .positionPredictionRangeEnd((short) 5)
                .dnfPoints((short) 1)
                .positionPoints((short) 1)
                .date("2024-07-15T12:30")
                .predictionStartDate("2024-07-15T12:30")
                .predictionEndDate("2024-07-18T12:30")
                .maxFreePredictions((short) 2)
                .isQualifier(true)
                .driverIds(List.of(1L, 2L))
                .questions(questionDtos.subList(2, 4))
                .build();

        raceService.createRace(raceDto1);
        raceService.createRace(raceDto2);
    }

    private List<QuestionDto> createQuestionDtos() {
        QuestionDto questionDto1 = QuestionDto.builder()
                .question("Who will win the race?")
                .points((short) 1)
                .build();

        QuestionDto questionDto2 = QuestionDto.builder()
                .question("Who will finish in second place?")
                .points((short) 1)
                .build();

        QuestionDto questionDto3 = QuestionDto.builder()
                .question("Who will finish in third place?")
                .points((short) 1)
                .build();

        QuestionDto questionDto4 = QuestionDto.builder()
                .question("Who will finish in fourth place?")
                .points((short) 1)
                .build();

        return List.of(questionDto1, questionDto2, questionDto3, questionDto4);
    }
}
