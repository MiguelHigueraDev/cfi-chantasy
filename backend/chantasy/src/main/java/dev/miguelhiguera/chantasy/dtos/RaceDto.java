package dev.miguelhiguera.chantasy.dtos;

import dev.miguelhiguera.chantasy.annotations.ValidDateRange;
import dev.miguelhiguera.chantasy.annotations.ValidPositionRange;
import dev.miguelhiguera.chantasy.dtos.predictions.QuestionDto;
import dev.miguelhiguera.chantasy.dtos.predictions.ResultDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidPositionRange
@ValidDateRange
public class RaceDto {

    @NotBlank(message = "El nombre de la carrera no puede estar vacío.")
    @Size(min = 3, max = 70, message = "El nombre de la carrera debe tener 3-70 caracteres.")
    private String name;

    @NotNull(message = "El código del circuito no puede estar vacío.")
    private Long circuitId;

    @NotNull(message = "El número máximo de DNF otorgado no puede estar vacío.")
    @Min(value = 1, message = "El número máximo de DNF otorgado debe ser mayor a 0.")
    @Max(value = 20, message = "El número máximo de DNF otorgado debe ser menor a 21.")
    private Short maxDnfAwarded;

    @NotNull(message = "El inicio del rango de posiciones que otorgan puntos si se predicen correctamente no puede estar vacío.")
    @Min(value = 1, message = "El inicio del rango de posiciones que otorgan puntos si se predicen correctamente debe ser mayor a 0.")
    @Max(value = 20, message = "El inicio del rango de posiciones que otorgan puntos si se predicen correctamente debe ser menor a 21.")
    private Short positionPredictionRangeStart;

    @NotNull(message = "El fin del rango de posiciones que otorgan puntos si se predicen correctamente no puede estar vacío.")
    @Min(value = 1, message = "El fin del rango de posiciones que otorgan puntos si se predicen correctamente debe ser mayor a 0.")
    @Max(value = 20, message = "El fin del rango de posiciones que otorgan puntos si se predicen correctamente debe ser menor a 21.")
    private Short positionPredictionRangeEnd;

    @NotNull(message = "Los puntos otorgados por DNF no pueden estar vacíos.")
    @Min(value = 1, message = "Los puntos otorgados por DNF deben ser mayores a 0.")
    @Max(value = 20, message = "Los puntos otorgados por DNF deben ser menores a 10.")
    private Short dnfPoints;

    @NotNull(message = "Los puntos otorgados por posición no pueden estar vacíos.")
    @Min(value = 1, message = "Los puntos otorgados por posición deben ser mayores a 0.")
    @Max(value = 10, message = "Los puntos otorgados por posición deben ser menores a 10.")
    private Short positionPoints;

    @NotBlank(message = "La fecha de la carrera no puede estar vacía.")
    private String date;

    @NotBlank(message = "La fecha de inicio de la predicción no puede estar vacía.")
    private String predictionStartDate;

    @NotBlank(message = "La fecha de fin de la predicción no puede estar vacía.")
    private String predictionEndDate;

    @NotNull(message = "El número máximo de predicciones libres no puede estar vacío.")
    @Range(min = 1, max = 10, message = "El número máximo de predicciones libres debe estar entre 1 y 10.")
    private Short maxFreePredictions;

    @NotNull(message = "El tipo de carrera no puede estar vacío.")
    private boolean isQualifier;

    @NotNull(message = "Las preguntas de la carrera no pueden estar vacías.")
    private List<QuestionDto> questions;

    @NotNull(message = "Los IDs de los pilotos no pueden estar vacíos.")
    private List<Long> driverIds;

    private List<ResultDto> results;
}
