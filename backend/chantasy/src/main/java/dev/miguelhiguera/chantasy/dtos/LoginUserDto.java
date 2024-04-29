package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotBlank(message = "Debes ingresar un nombre de usuario para iniciar sesión.")
    private String username;

    @NotBlank(message = "Debes ingresar una contraseña.")
    private String password;
}
