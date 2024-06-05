package org.pekka.geoanalyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.service.GeoAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GeoAnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoAnalyzerService geoAnalyzerService;

    private RestCountriesResponse restCountriesResponse;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/response-europe.json")));
        restCountriesResponse = objectMapper.readValue(json, RestCountriesResponse.class);
    }

    @Test
    void getDto() throws Exception {
//        Mockito.when(geoAnalyzerService.getDto()).thenReturn(ResponseEntity.ok(restCountriesResponse));
//
//        mockMvc.perform(get("/api/v1/geoanalyzer/processed2"))
//               .andExpect(status().isOk())
//               .andExpect(content().json(new ObjectMapper().writeValueAsString(restCountriesResponse)));
    }
}
