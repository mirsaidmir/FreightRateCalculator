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
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingApiClient {

    private final RestTemplate restTemplate;

    @Value("${geocoding.api.url}")
    private String apiBaseUrl;

    @Value("${geocoding.api.key}")
    private String apiKey;

    private final String PARAM_API_KEY = "apikey";
    private final String PARAM_GEO_CODE = "geocode";
    private final String PARAM_LANG = "lang";
    private final String LANG_RU = "ru_RU";
    private final String PARAM_FORMAT = "format";
    private final String JSON_TXT = "json";

    public Location fetchLocation(String cityName) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiBaseUrl)
                .queryParam(PARAM_API_KEY, apiKey)
                .queryParam(PARAM_GEO_CODE, cityName)
                .queryParam(PARAM_LANG, LANG_RU)
                .queryParam(PARAM_FORMAT, JSON_TXT)
                .build()
                .toUriString();
        ResponseEntity<GeoResponseDto> responseEntity =
                restTemplate.getForEntity(url, GeoResponseDto.class);
        GeoResponseDto geoResponseDto = responseEntity.getBody();

    }


}
