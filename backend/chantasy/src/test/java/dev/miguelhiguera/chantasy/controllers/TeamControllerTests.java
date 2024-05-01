package dev.miguelhiguera.chantasy.controllers;

import dev.miguelhiguera.chantasy.entities.Team;
import dev.miguelhiguera.chantasy.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class TeamControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setUp() {
        if (teamRepository.count() == 0) {
            Team team1 = new Team();
            team1.setName("Mercedes");
            team1.setLogoUrl("https://logo.com/mercedes");
            teamRepository.save(team1);

            Team team2 = new Team();
            team2.setName("Red Bull");
            team2.setLogoUrl("https://logo.com/redbull");
            teamRepository.save(team2);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllTeams_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Mercedes"))
                .andExpect(jsonPath("$[0].logoUrl").value("https://logo.com/mercedes"))
                .andExpect(jsonPath("$[1].name").value("Red Bull"))
                .andExpect(jsonPath("$[1].logoUrl").value("https://logo.com/redbull"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotReturnAllDrivers_whenUser() throws Exception {
        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTeam_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mercedes"))
                .andExpect(jsonPath("$.logoUrl").value("https://logo.com/mercedes"));
    }

    @Test
    void shouldNotReturnTeam_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateTeam_whenAdmin() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/teams/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"McLaren\", \"logoUrl\": \"https://logo.com/mclaren\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("{\"id\":3,\"name\":\"McLaren\",\"logoUrl\":\"https://logo.com/mclaren\",\"deleted\":false}", content);
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotCreateTeam_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"McLaren\", \"logoUrl\": \"https://logo.com/mclaren\"}"))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateTeam_whenAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Mercedes Benz\", \"logoUrl\": \"https://logo.com/mercedes\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mercedes Benz"))
                .andExpect(jsonPath("$.logoUrl").value("https://logo.com/mercedes"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotUpdateTeam_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Mercedes Benz\", \"logoUrl\": \"https://logo.com/mercedes\"}"))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteTeam_whenAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teams/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotDeleteTeam_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teams/1"))
                .andExpect(status().isBadRequest());
    }


}
