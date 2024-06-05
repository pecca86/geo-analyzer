package org.pekka.geoanalyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.Name;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GeoDataArrayMapper {

    GeoDataArrayMapper INSTANCE = Mappers.getMapper(GeoDataArrayMapper.class);

    @Mapping(target = "countryData", source = "restCountriesResponse")
    @Mapping(target = "message", constant = "Successfully processed the data.")
    @Mapping(target = "countryData.name", source = "restCountriesResponse")
    GeoDataResponse mapToGeoDataResponse(RestCountriesResponse restCountriesResponse);

    default String mapToName(Name name) {
        return name.common();
    }
}
