package org.pekka.geoanalyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.mapper.RestCountriesResponseMapper;
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

import static org.mockito.BDDMockito.given;
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
    private GeoDataResponse geoDataResponse;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/response-europe.json")));
        restCountriesResponse = objectMapper.readValue(json, RestCountriesResponse.class);
        geoDataResponse = RestCountriesResponseMapper.INSTANCE.mapToGeoDataResponse(restCountriesResponse, "Result Country");
    }

    @Test
    void should_trigger_new_job_when_no_active_jobs_and_not_when_job_already_started() throws Exception {
        // given
        // when
        mockMvc.perform(get("/api/v1/geoanalyzer"))
               .andExpect(status().isOk())
               .andExpect(content().string("Job started, you can check the result with api/v1/processed endpoint"));
        // then
        Mockito.verify(geoAnalyzerService, Mockito.times(1)).processGeoData();

        mockMvc.perform(get("/api/v1/geoanalyzer"))
               .andExpect(status().is(400))
               .andExpect(content().string("Job already started"));
    }

    @Test
    void should_return_processing_not_finished_response_when_no_result_yet() throws Exception {
        // given
        given(geoAnalyzerService.getResult()).willReturn(null);
        GeoDataResponse response = new GeoDataResponse("Processing is not finished yet", null, null);
        // when
        // then
        mockMvc.perform(get("/api/v1/geoanalyzer/processed"))
               .andExpect(status().is(202))
               .andExpect(content().json("{\"message\":\"Processing is not finished yet\"}"));
    }

    @Test
    void should_return_a_valid_geo_data_response_when_processing_is_finished() throws Exception {
        // given
        given(geoAnalyzerService.getResult()).willReturn(geoDataResponse);

        String json = new ObjectMapper().writeValueAsString(geoDataResponse);
        // when
        mockMvc.perform(get("/api/v1/geoanalyzer/processed"))
               .andExpect(status().isOk())
               .andExpect(content().json("{\"message\":\"Successfully processed the data.\"}"));
    }


}
