package org.pekka.geoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeoDataResponse(String message, List<GeoDataResponseItem> countryData, String countryWithMostNeighboursOfOtherRegion) {
}
