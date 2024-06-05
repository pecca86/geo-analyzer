package org.pekka.geoanalyzer.service;

import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.mapper.RestCountriesResponseMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class GeoAnalyzerService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GeoAnalyzerService.class);

    private final RestTemplate restTemplate;
    private CompletableFuture<RestCountriesResponse> futureResult;

    @Value("${rest.countries.api.url}")
    private String restCountriesUrl;

    @Autowired
    public GeoAnalyzerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @Retryable(retryFor = {CompletionException.class, ConnectException.class, ResourceAccessException.class, SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processGeoData() {
        futureResult = CompletableFuture
                .completedFuture(restTemplate.getForObject(restCountriesUrl, RestCountriesResponse.class));
    }

    public GeoDataResponse getResult() {
        if (futureResult == null || !futureResult.isDone()) {
            return null;
        } else {
            try {
                RestCountriesResponse futureResponse = futureResult.join();
                String resultCountry = calculateCountryWithMostNonSameRegionNeighbours(futureResponse);
                GeoDataResponse response = RestCountriesResponseMapper.INSTANCE.mapToGeoDataResponse(futureResponse, resultCountry);
                response.countryData().sort((a, b) -> (int) (b.population() - a.population()));
                return response;
            } catch (Exception e) {
                LOGGER.error("Error while processing geo data", e);
                return null;
            }
        }
    }

    private String calculateCountryWithMostNonSameRegionNeighbours(RestCountriesResponse restCountriesResponse) {
        List<String> asianCountryCodesList = restCountriesResponse.stream()
                                                                  .filter(country -> country.region().equals("Asia"))
                                                                  .map(GeoData::cca3)
                                                                  .toList();
        int currentMax = Integer.MIN_VALUE;
        String currentCountry = "";

        for (GeoData country : restCountriesResponse) {
            if (!"Asia".equals(country.region())) {
                continue;
            }
            List<String> neighbours = country.borders();
            int otherRegionNeighbourCount = 0;
            for (String neighbour : neighbours) {
                if (!asianCountryCodesList.contains(neighbour)) {
                    otherRegionNeighbourCount++;
                }
            }
            if (otherRegionNeighbourCount > currentMax) {
                currentMax = otherRegionNeighbourCount;
                currentCountry = country.name().common();
            }
        }
        return currentCountry;
    }
}
