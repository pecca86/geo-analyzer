package org.pekka.geoanalyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
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

    @Value("${rest.countries.api.url}")
    private String restCountriesUrl = "https://restcountries.com/v3.1/all?fields=population,name,region,borders,cca3";

    private RestCountriesResponse mockResponse;

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/all-name-region-borders-population-cca3.json")));
        mockResponse = objectMapper.readValue(json, RestCountriesResponse.class);
        // Inject the value of restCountriesUrl into geoAnalyzerService
        ReflectionTestUtils.setField(geoAnalyzerService, "restCountriesUrl", restCountriesUrl);
    }

    @Test
    public void should_get_a_valid_response_from_rest_countries() throws Exception {
        //given
        //when
        when(restTemplate.getForObject(anyString(), eq(RestCountriesResponse.class)))
                .thenReturn(mockResponse);

        CompletableFuture<Void> future = geoAnalyzerService.processGeoData();
        future.join();
        //then
        verify(restTemplate, times(1)).getForObject(restCountriesUrl, RestCountriesResponse.class);

        CompletableFuture<RestCountriesResponse> futureResult =
                (CompletableFuture<RestCountriesResponse>) ReflectionTestUtils.getField(geoAnalyzerService, "futureResult");
        assertNotNull(futureResult);
        assertTrue(futureResult.isDone());
        assertEquals(mockResponse, futureResult.get());
    }

    @Test
    public void should_get_valid_response_back_from_completable_future_and_map_it_to_a_GeoDataResponse_object() {
        //given
        //when
        when(restTemplate.getForObject(anyString(), eq(RestCountriesResponse.class)))
                .thenReturn(mockResponse);
        geoAnalyzerService.processGeoData().join();
        GeoDataResponse result = geoAnalyzerService.getResult();
        //then
        assertNotNull(result);
        assertEquals(250, result.getCountryData().size());
        assertEquals("Macau", result.getCountryData().getFirst().getName());
        assertEquals("Turkey", result.getCountryWithMostNeighboursOfOtherRegion());
    }

    @Test
    public void should_return_null_when_no_completable_futures_available() {
        GeoDataResponse result = geoAnalyzerService.getResult();
        assertNull(result);
    }
}
