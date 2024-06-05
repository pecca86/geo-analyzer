package org.pekka.geoanalyzer.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.pekka.geoanalyzer.dto.GeoData;
import org.pekka.geoanalyzer.dto.GeoDataArray;
import org.pekka.geoanalyzer.dto.GeoDataResponse;
import org.pekka.geoanalyzer.dto.GeoDataResponseItem;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T23:32:13+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class GeoDataArrayMapperImpl implements GeoDataArrayMapper {

    @Override
    public GeoDataResponse mapToGeoDataResponse(GeoDataArray geoDataArray) {
        if ( geoDataArray == null ) {
            return null;
        }

        List<GeoDataResponseItem> countryData = null;

        List<GeoDataResponseItem> list = geoDataArrayToGeoDataResponseItemList( geoDataArray );
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

    protected List<GeoDataResponseItem> geoDataArrayToGeoDataResponseItemList(GeoDataArray geoDataArray) {
        if ( geoDataArray == null ) {
            return null;
        }

        List<GeoDataResponseItem> list = new ArrayList<GeoDataResponseItem>( geoDataArray.size() );
        for ( GeoData geoData : geoDataArray ) {
            list.add( geoDataToGeoDataResponseItem( geoData ) );
        }

        return list;
    }
}
