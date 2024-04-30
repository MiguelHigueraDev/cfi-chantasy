package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryDto {

    @NotBlank(message = "El nombre del país no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del país debe tener 3-50 caracteres.")
    private String name;

    @NotBlank(message = "El código del país no puede estar vacío.")
    @Size(min = 2, max = 3, message = "El nombre del país debe tener 2-3 caracteres.")
    private String code;

    @NotBlank(message = "La URL de la bandera del país no puede estar vacía.")
    @Size(min = 255, message = "La URL de la bandera del país debe tener 255 caracteres como máximo.")
    private String flagUrl;
}
