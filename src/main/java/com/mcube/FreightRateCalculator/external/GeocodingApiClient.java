package com.mcube.FreightRateCalculator.external;

import com.mcube.FreightRateCalculator.dto.GeoResponseDto;
import com.mcube.FreightRateCalculator.entity.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GeocodingApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${geocoding.api.url}")
    private String apiBaseUrl;

    @Value("${geocoding.api.key}")
    private String apiKey;

    private final String PARAM_API_KEY = "apikey";
    private final String PARAM_GEO_CODE = "geocode";
    private final String PARAM_LANG = "lang";
    private final String PARAM_FORMAT = "format";

    private final String LANG_RU = "ru_RU";
    private final String JSON_TXT = "json";

    private final String CITY = "город";
    private final String CITY_DOT = "г.";
    private final String LOCALITY = "locality";

    public ResponseEntity<GeoResponseDto> fetchGeoResponseDto(String cityName) {
        return sendRequestWithCityIfMissing(cityName);

    }

    private ResponseEntity<GeoResponseDto> sendRequestWithCityIfMissing(String cityName) {
        return isCityWordPresent(cityName) ? sendRequest(cityName) : sendRequest(CITY + " " + cityName);
    }

    private boolean isCityWordPresent(String text) {
        return text.toLowerCase().contains(" " + CITY.toLowerCase() + " ") ||           //" город "
                text.toLowerCase().startsWith(CITY.toLowerCase()+ " ") ||               //"город " строго вначале, так как избегаем городов типа Новгород и т д
                text.toLowerCase().endsWith(" " + CITY.toLowerCase()) ||                //" город" аналогично избегаем Новгорода
                text.toLowerCase().contains(" " + CITY_DOT.toLowerCase() + " ") ||      // " г. "
                text.toLowerCase().startsWith(CITY_DOT.toLowerCase()) ||                // "г." вначале допускается
                text.toLowerCase().contains(" " + CITY_DOT.toLowerCase())               // " г." везде допускается/ отметаем варианты типа Гамбург.
        ;
    }

    private ResponseEntity<GeoResponseDto> sendRequest(String cityName) {
        String url = UriComponentsBuilder
                .fromUriString(apiBaseUrl)
                .queryParam(PARAM_API_KEY, apiKey)
                .queryParam(PARAM_GEO_CODE, cityName)
                .queryParam(PARAM_LANG, LANG_RU)
                .queryParam(PARAM_FORMAT, JSON_TXT)
                .build()
                .toUriString();
        log.debug("Sending request:\n" + url);
        ResponseEntity<GeoResponseDto> response = restTemplate.getForEntity(url, GeoResponseDto.class);
        log.info(response.getStatusCode() + " "
                + (response.getStatusCode().is2xxSuccessful() ? response.getBody().toString() : "") );
        return response;
    }

    public Optional<Location> getLocation(String locationName) {
        ResponseEntity<GeoResponseDto> response = fetchGeoResponseDto(locationName);
        if (response.getStatusCode().is2xxSuccessful()) {
            GeoResponseDto geoResponseDto = response.getBody();
            return parseGeoResponse(geoResponseDto);
        } else {
            //suggest user manually entering location data (lon, lad, name, etc)
            return Optional.empty();
        }

    }

    private Optional<Location> parseGeoResponse(GeoResponseDto geoResponse) {

        if (geoResponse == null) return Optional.empty();

        List<GeoResponseDto.FeatureMember> featureMembers = geoResponse.getResponse().geoObjectCollection.featureMember;
        Location location = null;
        for (GeoResponseDto.FeatureMember featureMember : featureMembers) {
            if (!featureMember.geoObject.metaDataProperty.geocoderMetaData.kind.equalsIgnoreCase(LOCALITY))
                continue;
            location = new Location();
            location.setCoordinates(featureMember.geoObject.point.pos);
            location.setNormalizedName(featureMember.geoObject.metaDataProperty.geocoderMetaData.address
                    .components.stream().filter(s->s.kind.equalsIgnoreCase(LOCALITY))
                    .findFirst().map(x->x.name).orElse(""));
            location.setDescription(featureMember.geoObject.metaDataProperty.geocoderMetaData.text);
            if (location.getNormalizedName().isBlank()) continue;
            break;
        }
        return Optional.of(location);
    }
}
