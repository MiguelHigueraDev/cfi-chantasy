package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

    @NotBlank(message = "La respuesta no puede estar vacía.")
    @Size(min = 1, max = 100, message = "La respuesta debe tener 1-100 caracteres.")
    private String answer;
}
