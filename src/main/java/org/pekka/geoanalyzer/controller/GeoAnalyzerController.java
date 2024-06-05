package org.pekka.geoanalyzer.controller;

import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.exception.JobAlreadyStartedException;
import org.pekka.geoanalyzer.exception.JobFailedException;
import org.pekka.geoanalyzer.service.GeoAnalyzerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/geoanalyzer")
public class GeoAnalyzerController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GeoAnalyzerController.class);

    private final GeoAnalyzerService geoAnalyzerService;
    private boolean jobStarted = false;

    @Autowired
    public GeoAnalyzerController(GeoAnalyzerService geoAnalyzerService) {
        this.geoAnalyzerService = geoAnalyzerService;
    }

    @GetMapping
    public ResponseEntity<String> initJob() {
        if (jobStarted) {
            LOGGER.error("Job already started");
            throw new JobAlreadyStartedException("Job already started");
        }
        jobStarted = true;
        geoAnalyzerService.processGeoData();
        return ResponseEntity.status(200).body("Job started, you can check the result with api/v1/processed endpoint");
    }

    @GetMapping("/processed")
    public ResponseEntity<GeoDataResponse> getProccessedGeoData() {
        GeoDataResponse response = geoAnalyzerService.getResult();
        if (response == null) {
            return ResponseEntity.status(202).body(new GeoDataResponse("Processing is not finished yet", null, null));
        }
        jobStarted = false;
        return ResponseEntity.status(200).body(geoAnalyzerService.getResult());
    }

}
