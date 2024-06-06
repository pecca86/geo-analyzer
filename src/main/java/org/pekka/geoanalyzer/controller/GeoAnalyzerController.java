package org.pekka.geoanalyzer.controller;

import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.exception.JobAlreadyStartedException;
import org.pekka.geoanalyzer.service.GeoAnalyzerService;
import org.pekka.geoanalyzer.service.JobStateService;
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
    private final JobStateService jobStateService;

    @Autowired
    public GeoAnalyzerController(GeoAnalyzerService geoAnalyzerService, JobStateService jobStateService) {
        this.geoAnalyzerService = geoAnalyzerService;
        this.jobStateService = jobStateService;
    }

    @GetMapping
    public ResponseEntity<String> initJob() {
        if (jobStateService.isJobStarted()) {
            LOGGER.error("Job already started");
            throw new JobAlreadyStartedException("Job already started");
        }
        jobStateService.startJob();
        geoAnalyzerService.processGeoData();
        return ResponseEntity.status(200).body("Job started, you can check the result with api/v1/processed endpoint");
    }

    @GetMapping("/processed")
    public ResponseEntity<GeoDataResponse> getProccessedGeoData() {
        if (!jobStateService.isJobStarted()) {
            return ResponseEntity.status(400).body(new GeoDataResponse("Job not started yet", null, null));
        }

        if (jobStateService.isJobFailed()) {
            jobStateService.resetJob();
            return ResponseEntity.status(500).body(new GeoDataResponse("Job failed", null, null));
        }

        GeoDataResponse response = geoAnalyzerService.getResult();
        if (response == null) {
            return ResponseEntity.status(202).body(new GeoDataResponse("Processing is not finished yet", null, null));
        }

        jobStateService.jobFinished(true);
        return ResponseEntity.status(200).body(geoAnalyzerService.getResult());
    }

    @GetMapping("/resetJob")
    public ResponseEntity<String> resetJob() {
        if (jobStateService.isJobSuccess() || jobStateService.isJobFailed() || !jobStateService.isJobStarted()) {
            jobStateService.resetJob();
            return ResponseEntity.status(200).body("Job reset");
        }
        return ResponseEntity.status(400).body("Wait for previous job to finish before resetting the job");
    }
}
