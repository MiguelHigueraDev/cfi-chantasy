package dev.miguelhiguera.chantasy.controllers;


import dev.miguelhiguera.chantasy.entities.Country;
import dev.miguelhiguera.chantasy.repositories.CountryRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
class CountryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    public void setUp() {
        if (countryRepository.count() > 0) {
            return;
        }

        Country country1 = new Country();
        country1.setName("Mexico");
        country1.setCode("MEX");
        country1.setFlagUrl("https://flag.com/mexico");
        countryRepository.save(country1);

        Country country2 = new Country();
        country2.setName("United States");
        country2.setCode("USA");
        country2.setFlagUrl("https://flag.com/usa");
        countryRepository.save(country2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllCountries_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/countries/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mexico"))
                .andExpect(jsonPath("$[0].code").value("MEX"))
                .andExpect(jsonPath("$[0].flagUrl").value("https://flag.com/mexico"))
                .andExpect(jsonPath("$[1].name").value("United States"))
                .andExpect(jsonPath("$[1].code").value("USA"))
                .andExpect(jsonPath("$[1].flagUrl").value("https://flag.com/usa"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotReturnAllCountries_whenUser() throws Exception {
        mockMvc.perform(get("/api/countries/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotReturnAllCountries_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/countries/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnCountry_whenLoggedIn() throws Exception {
        mockMvc.perform(get("/api/countries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mexico"))
                .andExpect(jsonPath("$.code").value("MEX"))
                .andExpect(jsonPath("$.flagUrl").value("https://flag.com/mexico"));
    }

    @Test
    void shouldReturnForbidden_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/countries/1"))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateCountry_whenLoggedIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/countries/")
                        .content("{\n" +
                                "    \"name\": \"Argentina\",\n" +
                                "    \"code\": \"ar\",\n" +
                                "    \"flagUrl\": \"test.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        // Asserting the response body content
        assertEquals("{\"id\":3,\"name\":\"Argentina\",\"code\":\"ar\",\"flagUrl\":\"test.com\",\"deleted\":false}", responseBody);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldNotCreateCountry_withInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/countries/").content(
                "{\n" +
                        "    \"name\": \"\",\n" +
                        "    \"code\": \"\",\n" +
                        "    \"flagUrl\": \"\"\n" +
                        "}"
                ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotCreateCountry_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/countries/").content(
                "{\n" +
                        "    \"name\": \"Canada\",\n" +
                        "    \"code\": \"CAN\",\n" +
                        "    \"flagUrl\": \"https://flag.com/canada\"\n" +
                        "}"
                ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateCountry_whenLoggedIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/countries/1")
                        .content("{\n" +
                                "    \"name\": \"Chile\",\n" +
                                "    \"code\": \"CHI\",\n" +
                                "    \"flagUrl\": \"https://flag.com/chile\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"Chile\",\"code\":\"CHI\",\"flagUrl\":\"https://flag.com/chile\",\"deleted\":false}", responseBody);
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotUpdateCountry_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/countries/1")
                        .content("{\n" +
                                "    \"name\": \"Chile\",\n" +
                                "    \"code\": \"CHI\",\n" +
                                "    \"flagUrl\": \"https://flag.com/chile\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteCountry_whenLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/countries/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/1")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldNotDeleteCountry_whenNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/countries/1"))
                .andExpect(status().isBadRequest());
    }

}