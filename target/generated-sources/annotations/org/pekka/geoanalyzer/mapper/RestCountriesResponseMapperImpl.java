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
    date = "2024-06-05T22:25:42+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class RestCountriesResponseMapperImpl implements RestCountriesResponseMapper {

    @Override
    public GeoDataResponse mapToGeoDataResponse(RestCountriesResponse restCountriesResponse, String resultCountry) {
        if ( restCountriesResponse == null && resultCountry == null ) {
            return null;
        }

        GeoDataResponse geoDataResponse = new GeoDataResponse();

        List<GeoDataResponseItem> list = restCountriesResponseToGeoDataResponseItemList( restCountriesResponse );
        if ( list != null ) {
            geoDataResponse.setCountryData( list );
        }
        if ( resultCountry != null ) {
            geoDataResponse.setCountryWithMostNeighboursOfOtherRegion( resultCountry );
        }
        geoDataResponse.setMessage( "Successfully processed the data." );

        return geoDataResponse;
    }

    protected GeoDataResponseItem geoDataToGeoDataResponseItem(GeoData geoData) {
        if ( geoData == null ) {
            return null;
        }

        String name = null;

        if ( geoData.name() != null ) {
            name = mapToName( geoData.name() );
        }

        double populationDensity = 0.0d;

        GeoDataResponseItem geoDataResponseItem = new GeoDataResponseItem( name, populationDensity );

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
