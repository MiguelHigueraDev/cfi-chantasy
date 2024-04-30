package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamDto {

    @NotBlank(message = "El nombre del país no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del equipo debe tener 3-50 caracteres.")
    private String name;

    @NotBlank(message = "La URL del logo del equipo no puede estar vacía.")
    @Size(min = 255, message = "La URL del logo del equipo debe tener 255 caracteres como máximo.")
    private String logoUrl;
}
