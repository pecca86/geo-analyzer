package org.pekka.geoanalyzer.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.GeoDataResponseItem;
import org.pekka.geoanalyzer.dto.RestCountriesResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-05T11:06:16+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class GeoDataArrayMapperImpl implements GeoDataArrayMapper {

    @Override
    public GeoDataResponse mapToGeoDataResponse(RestCountriesResponse restCountriesResponse) {
        if ( restCountriesResponse == null ) {
            return null;
        }

        List<GeoDataResponseItem> countryData = null;

        List<GeoDataResponseItem> list = restCountriesResponseToGeoDataResponseItemList( restCountriesResponse );
        if ( list != null ) {
            countryData = list;
        }

        String message = "Successfully processed the data.";

        GeoDataResponse geoDataResponse = new GeoDataResponse( message, countryData );

        return geoDataResponse;
    }

    protected GeoDataResponseItem geoDataToGeoDataResponseItem(GeoData geoData) {
        if ( geoData == null ) {
            return null;
        }

        String name = null;
        Long population = null;

        if ( geoData.name() != null ) {
            name = mapToName( geoData.name() );
        }
        if ( geoData.population() != null ) {
            population = geoData.population();
        }

        GeoDataResponseItem geoDataResponseItem = new GeoDataResponseItem( name, population );

        return geoDataResponseItem;
    }

    protected List<GeoDataResponseItem> restCountriesResponseToGeoDataResponseItemList(RestCountriesResponse restCountriesResponse) {
        if ( restCountriesResponse == null ) {
            return null;
        }

        List<GeoDataResponseItem> list = new ArrayList<GeoDataResponseItem>( restCountriesResponse.size() );
        for ( GeoData geoData : restCountriesResponse ) {
            list.add( geoDataToGeoDataResponseItem( geoData ) );
        }

        return list;
    }
}
