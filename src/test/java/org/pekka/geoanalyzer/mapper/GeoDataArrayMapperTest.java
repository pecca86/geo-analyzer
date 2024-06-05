package org.pekka.geoanalyzer.mapper;

import org.junit.jupiter.api.Test;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.GeoDataArray;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GeoDataArrayMapperTest {



    @Test
    void should_map_geo_data_array_to_geo_data_response() {
        // given
        GeoDataArray geoDataArray = new GeoDataArray();
        Name name = new Name("Country Name");
        GeoData geoData = new GeoData(name,"Europe", List.of("FIN", "RUS", "SWE"), 3000L);
        geoDataArray.add(geoData);

        GeoDataResponse expected = GeoDataArrayMapper.INSTANCE.mapToGeoDataResponse(geoDataArray);

        assertThat(expected).isNotNull();
        assertThat(expected.countryData()).isNotNull();
        assertThat(expected.countryData().size()).isEqualTo(1);
        assertThat(expected.countryData().get(0).name()).isEqualTo("Country Name");
        assertThat(expected.countryData().get(0).population()).isEqualTo(3000L);
    }
}
