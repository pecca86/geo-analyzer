package org.pekka.geoanalyzer.controller;

import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.service.GeoAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/geoanalyzer")
public class GeoAnalyzerController {

    private final GeoAnalyzerService geoAnalyzerService;

    @Autowired
    public GeoAnalyzerController(GeoAnalyzerService geoAnalyzerService) {
        this.geoAnalyzerService = geoAnalyzerService;
    }

    @GetMapping
    public String initJob() {
//        GeoData geoData = restTemplateBuilderConfig.restTemplate().getForObject("https://api.ipgeolocation.io/ipgeo?apiKey=API_KEY", GeoData.class);

//        GeoData geoData = restTemplate.getForObject("https://restcountries.com/v3.1/region/europe?fields=borders,region,population,name", GeoData.class);
//
//        return Optional.ofNullable(geoData)
//                       .map(GeoData::toString)
//                       .orElse("No data found");

        geoAnalyzerService.processGeoData();

        return "Job started, you can check the result with api/v1/processed endpoint";
    }

    @GetMapping("/processed")
    public ResponseEntity<GeoDataResponse> getProccessedGeoData() {
        GeoDataResponse response = geoAnalyzerService.getFutureResult();
        if (response == null) {
            return ResponseEntity.status(202).body(new GeoDataResponse("Processing is not finished yet", null));
        }
         return ResponseEntity.status(200).body(geoAnalyzerService.getFutureResult());
    }

}
