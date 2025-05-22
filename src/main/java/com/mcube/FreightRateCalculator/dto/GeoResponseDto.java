package com.mcube.FreightRateCalculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResponseDto {
    public Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        @JsonProperty("GeoObjectCollection")
        public GeoObjectCollection geoObjectCollection;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoObjectCollection {
        public List<FeatureMember> featureMember;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeatureMember{
        @JsonProperty("GeoObject")
        public GeoObject geoObject;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoObject {

        public MetaDataProperty metaDataProperty;
        public String name;
        @JsonProperty("Point")
        public Point point;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaDataProperty {
        @JsonProperty("GeocoderMetaData")
        public GeocoderMetaData geocoderMetaData;

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        public String pos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeocoderMetaData {
        public String text;
        public String kind;
        @JsonProperty("Address")
        public Address address;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {

        @JsonProperty("country_code")
        public String countryCode;
        @JsonProperty("Components")
        public List<Component> components;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Component {
        public String kind;
        public String name;
    }
}
