package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionsDto {

    private List<AnswerDto> answers;
    private List<FreePredictionDto> freePredictions;
    private List<ResultDto> results;

    @NotNull(message = "El ID de la carrera no puede estar vacío")
    private Long raceId;

}
