package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    // Pass race ID as a path variable

    @Size(min = 3, max = 100, message = "La pregunta debe tener entre 3 y 100 caracteres")
    @NotBlank(message = "La pregunta no puede estar vacía")
    private String question;

    @Range(min = 1, max = 10, message = "Los puntos deben estar entre 1 y 10")
    @NotNull(message = "Los puntos de la pregunta no pueden estar vacíos")
    private Short points;
}
