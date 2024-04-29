package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.dtos.LoginUserDto;
import dev.miguelhiguera.chantasy.dtos.RegisterUserDto;
import dev.miguelhiguera.chantasy.entities.User;
import dev.miguelhiguera.chantasy.responses.LoginResponse;
import dev.miguelhiguera.chantasy.services.AuthenticationService;
import dev.miguelhiguera.chantasy.services.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testRegister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        User user = new User();

        Mockito.when(authenticationService.register(Mockito.any(RegisterUserDto.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto();
        User user = new User();
        LoginResponse loginResponse = new LoginResponse("token", 3600);

        Mockito.when(authenticationService.login(Mockito.any(LoginUserDto.class))).thenReturn(user);
        Mockito.when(jwtService.generateToken(Mockito.any(User.class))).thenReturn("token");
        Mockito.when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         {
                          "username": "username",
                          "password": "password"
                         }
                         """))
                .andExpect(status().isOk());
    }
}