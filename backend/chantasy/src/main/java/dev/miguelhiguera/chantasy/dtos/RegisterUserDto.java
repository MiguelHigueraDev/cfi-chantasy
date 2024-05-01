package dev.miguelhiguera.chantasy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    @NotBlank(message = "Debes ingresar un nombre de usuario para iniciar sesión.")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,15}$", message = "El nombre de usuario para iniciar sesión debe contener entre 4 y 15 caracteres, y solo puede contener letras, números y guiones bajos.")
    private String username;

    @NotBlank(message = "Debes ingresar una contraseña.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "La contraseña debe contener al menos 8 caracteres, en los que debe haber por lo menos letra minúscula, una letra mayúscula y un número.")
    private String password;

    @NotBlank(message = "Debes ingresar un nombre de usuario visible.")
    @Pattern(regexp = "^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*$", message = "El nombre de usuario visible debe contener solo letras, números y espacios, y debe tener entre 4 y 25 caracteres.")
    private String displayName;
}
