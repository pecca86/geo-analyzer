package org.pekka.geoanalyzer.controller;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class MarshallTest {

    @Autowired
    JacksonTester<RestCountriesResponse> tester;

    @Test
    void marshallGeoDataArray() throws IOException {
        // Given
        byte[] bytes = Files.readAllBytes(Paths.get("src/test/java/org/pekka/geoanalyzer/testdata/response-europe.json"));
        // When
        ObjectContent<RestCountriesResponse> content = tester.parse(bytes);
        // Then
        content.assertThat().isInstanceOf(RestCountriesResponse.class);
        content.assertThat().asInstanceOf(InstanceOfAssertFactories.type(RestCountriesResponse.class)).satisfies(geoDataArray -> {
            assertThat(geoDataArray).isNotNull();
            assertThat(geoDataArray).extracting(
                    GeoData::region,
                    GeoData::population,
                    gd -> gd.name().common(),
                    gd -> gd.borders().size()
            ).containsExactly(Tuple.tuple(
                    "Europe",
                    100800L,
                    "Jersey",
                    0)
            );
        });
    }
}
