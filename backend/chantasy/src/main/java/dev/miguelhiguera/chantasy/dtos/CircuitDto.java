package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CircuitDto {

    @NotBlank(message = "El nombre del circuito no puede estar vacío.")
    @Size(min = 3, max = 70, message = "El nombre del circuito debe tener 3-70 caracteres.")
    private String name;

    @NotNull(message = "El país del circuito no puede estar vacío.")
    private Long countryId;
}
