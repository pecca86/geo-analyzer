package org.pekka.geoanalyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoAnalyzerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeoAnalyzerService geoAnalyzerService;

    private RestCountriesResponse mockResponse;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/all-name-region-borders-population-cca3.json")));
        mockResponse = objectMapper.readValue(json, RestCountriesResponse.class);
    }

    @Test
    public void should_invoke_a_new_async_job() {
        // given
        // when
        when(restTemplate.getForObject(anyString(), eq(RestCountriesResponse.class)))
                .thenReturn(mockResponse);
//        CompletableFuture<Void> future = geoAnalyzerService.processGeoData();
//        future.join();
        // then
        verify(restTemplate, times(1)).getForObject(anyString(), eq(RestCountriesResponse.class));
    }

    @Test
    public void should_return_a_valid_geo_data_response() {
        // given
        // when
        when(restTemplate.getForObject(anyString(), eq(RestCountriesResponse.class)))
                .thenReturn(mockResponse);

//        geoAnalyzerService.processGeoData().join();
        GeoDataResponse result = geoAnalyzerService.getResult();
        // then
        assertNotNull(result);
        assertEquals(250, result.countryData().size());
        assertEquals("China", result.countryData().get(0).name());
        assertEquals("Turkey", result.countryWithMostNeighboursOfOtherRegion());
    }

    @Test
    public void should_return_null_while_still_processing_the_request() {
        // given
        // when
        GeoDataResponse result = geoAnalyzerService.getResult();
        // then
        assertNull(result);
    }


}
