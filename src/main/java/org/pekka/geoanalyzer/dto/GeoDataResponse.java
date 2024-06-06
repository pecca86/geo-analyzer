package org.pekka.geoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoDataResponse {

    private String message;
    private List<GeoDataResponseItem> countryData;
    private String countryWithMostNeighboursOfOtherRegion;

    public GeoDataResponse() {
    }

    public GeoDataResponse(String message, List<GeoDataResponseItem> countryData, String countryWithMostNeighboursOfOtherRegion) {
        this.countryData = countryData;
        this.countryWithMostNeighboursOfOtherRegion = countryWithMostNeighboursOfOtherRegion;
        this.message = message;
    }

    public List<GeoDataResponseItem> getCountryData() {
        return countryData;
    }

    public void setCountryData(List<GeoDataResponseItem> countryData) {
        this.countryData = countryData;
    }

    public String getCountryWithMostNeighboursOfOtherRegion() {
        return countryWithMostNeighboursOfOtherRegion;
    }

    public void setCountryWithMostNeighboursOfOtherRegion(String countryWithMostNeighboursOfOtherRegion) {
        this.countryWithMostNeighboursOfOtherRegion = countryWithMostNeighboursOfOtherRegion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
