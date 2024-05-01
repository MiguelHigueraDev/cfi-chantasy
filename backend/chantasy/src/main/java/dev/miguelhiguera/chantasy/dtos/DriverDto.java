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
public class DriverDto {

    @NotBlank(message = "El nombre del piloto no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del piloto debe tener 3-50 caracteres.")
    private String name;

    @NotBlank(message = "El código del piloto no puede estar vacío.")
    @Size(min = 2, max = 3, message = "El código del piloto debe tener 2-3 caracteres.")
    private String code;

    @NotNull(message = "El país del piloto no puede estar vacío.")
    private Long countryId;

    @NotNull(message = "El equipo del piloto no puede estar vacío.")
    private Long teamId;


}
