package com.mcube.FreightRateCalculator.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcube.FreightRateCalculator.dto.ErrorGeoResponseDto;
import com.mcube.FreightRateCalculator.dto.GeoResponseDto;
import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.exceptionHandling.GeocodingApiClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class GeocodingApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
    private final String PROVINCE = "province";

    private final String GEOCODING_API_PARSE_ERROR_MESSAGE = "Не удалось распарсить ошибку Yandex Geocoding API";
    private final String GEOCODING_API_REQUEST_IS_NOT_SUCCESSFUL = "запрос Yandex Geocoding API вернулся с ошибкой";

    public ResponseEntity<GeoResponseDto> fetchGeoResponseDto(String cityName) {
        return sendRequestWithCityIfMissing(cityName);

    }

    private ResponseEntity<GeoResponseDto> sendRequestWithCityIfMissing(String cityName) {
        return isCityWordPresent(cityName) ? sendRequest(cityName) : sendRequest(CITY + " " + cityName);
    }

    private boolean isCityWordPresent(String text) {
        return text.toLowerCase().contains(" " + CITY.toLowerCase() + " ") ||           //" город "
                text.toLowerCase().startsWith(CITY.toLowerCase() + " ") ||               //"город " строго вначале, так как избегаем городов типа Новгород и т д
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

        ResponseEntity<GeoResponseDto> response = null;

        try {
            response = restTemplate.getForEntity(url, GeoResponseDto.class);
        } catch (HttpClientErrorException exception) {
            String errorJson = exception.getResponseBodyAsString();
            try {
                ErrorGeoResponseDto errorGeoResponseDto = objectMapper.readValue(errorJson, ErrorGeoResponseDto.class);
                throw new GeocodingApiClientException(errorGeoResponseDto);
            } catch (JsonProcessingException e) {
                throw new GeocodingApiClientException(GEOCODING_API_PARSE_ERROR_MESSAGE);
            }
        }

        return response;
    }

    public Optional<Location> getLocation(String locationName) {
        ResponseEntity<GeoResponseDto> response = fetchGeoResponseDto(locationName);
        HttpHeaders headers = response.getHeaders();

        if (response.getStatusCode().is2xxSuccessful()) {
            GeoResponseDto geoResponseDto = response.getBody();
            return buildLocationFromGeoResponse(geoResponseDto);
        } else {
            throw new GeocodingApiClientException(GEOCODING_API_REQUEST_IS_NOT_SUCCESSFUL);
            //suggest user manually entering location data (lon, lad, name, etc)
            //return Optional.empty();
        }

    }

    private Optional<Location> buildLocationFromGeoResponse(GeoResponseDto geoResponse) {

        if (geoResponse == null || geoResponse.getResponse() == null) return Optional.empty();
        GeoResponseDto.Response responseDto = geoResponse.getResponse();

        if (responseDto.geoObjectCollection == null || responseDto.geoObjectCollection.featureMember == null)
            return Optional.empty();

        String normalizedName = "";
        String coordinates = "";
        String description = "";

        List<GeoResponseDto.FeatureMember> featureMembers = responseDto.geoObjectCollection.featureMember;
        Location location = null;
        for (GeoResponseDto.FeatureMember featureMember : featureMembers) {

            if (featureMember == null || featureMember.geoObject == null
                    || featureMember.geoObject.metaDataProperty == null
                    || featureMember.geoObject.metaDataProperty.geocoderMetaData == null
                    || featureMember.geoObject.metaDataProperty.geocoderMetaData.kind == null) continue;

            if (!featureMember.geoObject.metaDataProperty.geocoderMetaData.kind.equalsIgnoreCase(LOCALITY) &&
                    !featureMember.geoObject.metaDataProperty.geocoderMetaData.kind.equalsIgnoreCase(PROVINCE))
                continue;

            if (featureMember.geoObject.point == null) continue;

            coordinates = featureMember.geoObject.point.pos;
            normalizedName = featureMember.geoObject.name;
            description = featureMember.geoObject.metaDataProperty.geocoderMetaData.text;

            if (normalizedName == null || normalizedName.isBlank()) continue;
            break;
        }

        location = new Location(normalizedName, coordinates, description);

        return Optional.of(location);
    }


}
