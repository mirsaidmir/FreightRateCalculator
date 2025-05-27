package com.mcube.FreightRateCalculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TomtomRoutes {

    private String formatVersion;
    private List<Route> routes;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private Summary summary;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        private BigDecimal lengthInMeters;
        private BigDecimal travelTimeInSeconds;
    }
}
