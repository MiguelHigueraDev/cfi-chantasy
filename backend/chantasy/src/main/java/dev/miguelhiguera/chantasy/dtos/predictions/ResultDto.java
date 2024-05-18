package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDto {
    @NotNull(message = "El id de la carrera no puede ser nulo.")
    private Long raceId;

    @NotNull(message = "El id del piloto no puede ser nulo.")
    private Long driverId;

    @NotNull(message = "La posición del piloto no puede ser nula.")
    private Short position;
}
