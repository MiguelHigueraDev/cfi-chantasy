package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CircuitDto {

    @NotBlank(message = "El nombre del circuito no puede estar vacío.")
    @Size(min = 3, max = 70, message = "El nombre del circuito debe tener 3-70 caracteres.")
    private String name;

    @NotBlank(message = "El país del circuito no puede estar vacío.")
    private Long countryId;
}
