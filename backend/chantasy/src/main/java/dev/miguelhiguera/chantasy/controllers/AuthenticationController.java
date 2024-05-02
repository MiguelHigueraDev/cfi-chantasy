package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.LoginUserDto;
import dev.miguelhiguera.chantasy.dtos.RegisterUserDto;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.responses.LoginResponse;
import dev.miguelhiguera.chantasy.services.impl.AuthenticationService;
import dev.miguelhiguera.chantasy.services.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User user = authenticationService.register(registerUserDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        User user = authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
