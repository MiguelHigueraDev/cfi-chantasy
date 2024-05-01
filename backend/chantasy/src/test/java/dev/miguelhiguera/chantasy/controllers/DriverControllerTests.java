package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.entities.Driver;
import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
import dev.miguelhiguera.chantasy.repositories.DriverRepository;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class DriverControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setUp() {
        if (driverRepository.count() == 0) {
            Team team = new Team();
            team.setName("Mercedes");
            team.setLogoUrl("https://logo.com/mercedes");
            teamRepository.save(team);

            Country country = new Country();
            country.setName("Mexico");
            country.setCode("MEX");
            country.setFlagUrl("https://flag.com/mexico");
            countryRepository.save(country);

            Driver driver = new Driver();
            driver.setName("Lewis Hamilton");
            driver.setCode("HAM");
            driver.setTeam(team);
            driver.setCountry(country);
            driverRepository.save(driver);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllDrivers_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/drivers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lewis Hamilton"))
                .andExpect(jsonPath("$[0].code").value("HAM"))
                .andExpect(jsonPath("$[0].team.name").value("Mercedes"))
                .andExpect(jsonPath("$[0].team.logoUrl").value("https://logo.com/mercedes"))
                .andExpect(jsonPath("$[0].country.name").value("Mexico"))
                .andExpect(jsonPath("$[0].country.code").value("MEX"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotReturnAllDrivers_whenUser() throws Exception {
        mockMvc.perform(get("/api/drivers/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnDriver_whenLoggedIn() throws Exception {
        mockMvc.perform(get("/api/drivers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lewis Hamilton"))
                .andExpect(jsonPath("$.code").value("HAM"))
                .andExpect(jsonPath("$.team.name").value("Mercedes"))
                .andExpect(jsonPath("$.team.logoUrl").value("https://logo.com/mercedes"))
                .andExpect(jsonPath("$.country.name").value("Mexico"))
                .andExpect(jsonPath("$.country.code").value("MEX"));
    }

    @Test
    void shouldNotReturnAllDrivers_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/drivers/"))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateDriver_whenLoggedIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/")
                .contentType("application/json")
                .content("{\"name\": \"Max Verstappen\", \"code\": \"VER\", \"teamId\": 1, \"countryId\": 1}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("{\"id\":2,\"name\":\"Max Verstappen\",\"code\":\"VER\",\"country\":{\"id\":1,\"name\":\"Mexico\",\"code\":\"MEX\",\"flagUrl\":\"https://flag.com/mexico\",\"deleted\":false},\"team\":{\"id\":1,\"name\":\"Mercedes\",\"logoUrl\":\"https://logo.com/mercedes\",\"deleted\":false},\"deleted\":false}", content);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldNotCreateDriver_whenTeamNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/")
                .contentType("application/json")
                .content("{\"name\": \"Max Verstappen\", \"code\": \"VER\", \"teamId\": 2, \"countryId\": 1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldNotCreateDriver_whenCountryNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/")
                .contentType("application/json")
                .content("{\"name\": \"Max Verstappen\", \"code\": \"VER\", \"teamId\": 1, \"countryId\": 2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldNotCreateDriver_withInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/")
                .contentType("application/json")
                .content("{\"name\": \"\", \"code\": \"\", \"teamId\": 1, \"countryId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotCreateDriver_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/")
                .contentType("application/json")
                .content("{\"name\": \"Max Verstappen\", \"code\": \"VER\", \"teamId\": 1, \"countryId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteDriver_whenLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers/1")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotDeleteDriver_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/1"))
                .andExpect(status().isBadRequest());
    }
}
