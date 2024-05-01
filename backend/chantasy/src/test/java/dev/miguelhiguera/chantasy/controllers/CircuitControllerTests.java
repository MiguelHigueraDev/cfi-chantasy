package dev.miguelhiguera.chantasy.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.miguelhiguera.chantasy.entities.Circuit;
import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CircuitRepository;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
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
public class CircuitControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CircuitRepository circuitRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    public void setUp() {
        if (circuitRepository.count() == 0) {
            Country country = new Country();
            country.setName("Chile");
            country.setCode("CHI");
            country.setFlagUrl("https://flag.com/chile");
            countryRepository.save(country);

            Circuit circuit1 = new Circuit();
            circuit1.setName("Circuito Cueca");
            circuit1.setCountry(country);
            circuitRepository.save(circuit1);

            Circuit circuit2 = new Circuit();
            circuit2.setName("Circuito 2");
            circuit2.setCountry(country);
            circuitRepository.save(circuit2);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllCircuits_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/circuits/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Circuito Cueca"))
                .andExpect(jsonPath("$[1].name").value("Circuito 2"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotReturnAllCircuits_whenUser() throws Exception {
        mockMvc.perform(get("/api/circuits/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotReturnCircuit_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/circuits/1"))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateCircuit_whenAdmin() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/circuits/")
                .contentType("application/json")
                .content("{\"name\": \"Circuito 3\", \"countryId\": 1}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);

        assertEquals("Circuito 3", jsonNode.get("name").asText());
        assertEquals(1, jsonNode.get("country").get("id").asLong());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotCreateCircuit_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/circuits/")
                .contentType("application/json")
                .content("{\"name\": \"Circuito 3\", \"countryId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateCircuit_whenAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/circuits/1")
                .contentType("application/json")
                .content("{\"name\": \"Circuito 4\", \"countryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Circuito 4"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotUpdateCircuit_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/circuits/1")
                .contentType("application/json")
                .content("{\"name\": \"Circuito 4\", \"countryId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteCircuit_whenAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/circuits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotDeleteCircuit_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/circuits/1"))
                .andExpect(status().isBadRequest());
    }
}
