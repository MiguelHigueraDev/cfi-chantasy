package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreePredictionDto {

    @NotBlank(message = "La predicción no puede estar vacía.")
    @Size(min = 1, max = 100, message = "La predicción debe tener 1-100 caracteres.")
    private String prediction;
}
