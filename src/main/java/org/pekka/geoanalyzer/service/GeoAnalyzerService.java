package org.pekka.geoanalyzer.service;

import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.mapper.GeoDataArrayMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class GeoAnalyzerService {

    private final RestTemplate restTemplate;
    private CompletableFuture<RestCountriesResponse> futureResult;

    @Autowired
    public GeoAnalyzerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    @Retryable(retryFor = {ConnectException.class, ResourceAccessException.class, SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void processGeoData() {
        try {
            futureResult = CompletableFuture
                .completedFuture(restTemplate.getForObject("https://restcountries.com/v3.1/all?fields=population,name,region,borders,cca3", RestCountriesResponse.class));
        } catch (CompletionException e) {
            System.out.println("Error occurred while processing the data");
        }
    }

    public GeoDataResponse getFutureResult() {
        if (futureResult == null) {
            return null;
            // throw JobNotFinishedException
        } else if (!futureResult.isDone()) {
            return null;
            // throw JobNotFinishedException
        } else {
            try {
                GeoDataResponse response = GeoDataArrayMapper.INSTANCE.mapToGeoDataResponse(futureResult.join());
                response.countryData().sort((a, b) -> (int) (b.population() - a.population()));
                return response;
            } catch (Exception e) {
                return null;
                // throw DataProcessingException
            }
        }
    }

    public ResponseEntity<RestCountriesResponse> getDto() {
        return restTemplate.getForEntity("https://restcountries.com/v3.1/region/europe?fields=borders,region,population,name", RestCountriesResponse.class);
    }
}
