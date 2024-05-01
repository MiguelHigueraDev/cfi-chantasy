package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank(message = "Debes ingresar un nombre de usuario para iniciar sesión.")
    private String username;

    @NotBlank(message = "Debes ingresar una contraseña.")
    private String password;
}
