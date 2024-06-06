package org.pekka.geoanalyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.mapper.RestCountriesResponseMapper;
import org.pekka.geoanalyzer.service.GeoAnalyzerService;
import org.pekka.geoanalyzer.service.JobStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class GeoAnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoAnalyzerService geoAnalyzerService;
    @MockBean
    private JobStateService jobStateService;

    private RestCountriesResponse restCountriesResponse;
    private GeoDataResponse geoDataResponse;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/response-europe.json")));
        restCountriesResponse = objectMapper.readValue(json, RestCountriesResponse.class);
        geoDataResponse = RestCountriesResponseMapper.INSTANCE.mapToGeoDataResponse(restCountriesResponse, "Result Country");
        RestAssuredMockMvc.reset();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void should_trigger_new_job_when_no_active_jobs() {
        // given
        given(jobStateService.isJobStarted()).willReturn(false);
        // when
        // then
        RestAssuredMockMvc.given()
                          .when()
                          .get("/api/v1/geoanalyzer")
                          .then()
                          .statusCode(200);
    }

    @Test
    void should_not_trigger_a_new_job_when_active_job_found() {
        // given
        given(jobStateService.isJobStarted()).willReturn(true);
        // when
        // then
        RestAssuredMockMvc.given()
                          .when()
                          .get("/api/v1/geoanalyzer")
                          .then()
                          .statusCode(400);
    }

    @Test
    void should_return_processing_not_finished_response_when_no_result_yet() {
        // given
        given(geoAnalyzerService.getResult()).willReturn(null);
        given(jobStateService.isJobStarted()).willReturn(true);
        // when
        // then
        RestAssuredMockMvc.given()
                          .when()
                          .get("/api/v1/geoanalyzer/processed")
                          .then()
                          .statusCode(202);
    }

    @Test
    void should_return_a_valid_geo_data_response_when_processing_is_finished() {
        // given
        given(geoAnalyzerService.getResult()).willReturn(geoDataResponse);
        given(jobStateService.isJobStarted()).willReturn(true);
        // when
        // then
        RestAssuredMockMvc.given()
                          .when()
                          .get("/api/v1/geoanalyzer/processed")
                          .then()
                          .statusCode(200)
                          .body("message", equalTo("Successfully processed the data."));

    }

}
