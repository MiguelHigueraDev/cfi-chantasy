package dev.miguelhiguera.chantasy.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private long expiresIn;
    private String refreshToken;


}
