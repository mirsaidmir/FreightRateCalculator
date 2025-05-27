package com.mcube.FreightRateCalculator.external;

import com.mcube.FreightRateCalculator.dto.TomtomRoutes;
import com.mcube.FreightRateCalculator.entity.Distance;
import com.mcube.FreightRateCalculator.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class TomtomClient {


    @Autowired
    private WebClient webClient;

    @Value("${tomtom.api.key}")
    private String apiKey;

    @Value("${tomtom.api.baseUrl}")
    private String apiBaseUrl;

    private final String PARAM_TRAVEL_MODE = "travelMode";
    private final String PARAM_TRUCK = "truck";
    private final String PARAM_KEY = "key";
    private final String PARAM_ROUTE_REPRESENTATION = "routeRepresentation";
    private final String PARAM_SUMMARY_ONLY = "summaryOnly";
    private final String PARAM_TRAFFIC = "traffic";


    public Optional<Distance> fetchDistance(Location origin, Location destination) {
        TomtomRoutes response = fetchTomtomRoutes(origin, destination);
        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
            return Optional.empty();
        }
        Distance dist = new Distance();
        dist.setOrigin(origin);
        dist.setDestination(destination);
        BigDecimal distanceInKm = response.getRoutes().get(0).getSummary().getLengthInMeters()
                .divide(BigDecimal.valueOf(1000), 2, RoundingMode.DOWN);
        dist.setDistance(distanceInKm);
        dist.setFetchDate(LocalDate.now());
        return Optional.of(dist);
    }

    private TomtomRoutes fetchTomtomRoutes(Location origin, Location destination) {
        String route = buildLocations(origin.getCoordinates(),destination.getCoordinates());
        String url = UriComponentsBuilder
                .fromUriString(apiBaseUrl+ "/" + route + "/json")
                .queryParam(PARAM_TRAVEL_MODE, PARAM_TRUCK)
                .queryParam(PARAM_KEY, apiKey)
                .queryParam(PARAM_ROUTE_REPRESENTATION, PARAM_SUMMARY_ONLY)
                .queryParam(PARAM_TRAFFIC, "true")
                .build()
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(TomtomRoutes.class)
                .block();   //синхронно
    }

    private String buildLocations(String origin,String destination) {
        String[] partsOrigin = origin.replace(",",".").split(" ");
        String[] partsDestination = destination.replace(",",".").split(" ");
        return partsOrigin[1] + "," + partsOrigin[0] + ":" + partsDestination[1] + "," + partsDestination[0];
    }
}
