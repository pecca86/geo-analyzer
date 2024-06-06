package org.pekka.geoanalyzer.mapper;

import org.junit.jupiter.api.Test;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.Name;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RestCountriesResponseMapperTest {



    @Test
    void should_map_geo_data_array_to_geo_data_response() {
        // given
        RestCountriesResponse restCountriesResponse = new RestCountriesResponse();
        Name name = new Name("Country Name");
        GeoData geoData = new GeoData(name,"Europe", List.of("FIN", "RUS", "SWE"), 3000L, "NOR", 1000.0);
        restCountriesResponse.add(geoData);

        GeoDataResponse expected = RestCountriesResponseMapper.INSTANCE.mapToGeoDataResponse(restCountriesResponse, "Result Country");

        assertThat(expected).isNotNull();
        assertThat(expected.getCountryData()).isNotNull();
        assertThat(expected.getCountryData().size()).isEqualTo(1);
        assertThat(expected.getCountryData().getFirst().getName()).isEqualTo("Country Name");
        assertThat(expected.getCountryWithMostNeighboursOfOtherRegion()).isEqualTo("Result Country");
    }
}
