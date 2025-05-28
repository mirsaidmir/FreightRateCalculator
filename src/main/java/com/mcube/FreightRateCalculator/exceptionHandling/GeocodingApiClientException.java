package com.mcube.FreightRateCalculator.exceptionHandling;

import com.mcube.FreightRateCalculator.dto.ErrorGeoResponseDto;
import com.mcube.FreightRateCalculator.external.GeocodingApiClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class GeocodingApiClientException extends RuntimeException {
    private HttpStatusCode statusCode;
    public GeocodingApiClientException(String message) {
        super(message);
    }

    public GeocodingApiClientException(ErrorGeoResponseDto errorResponse) {
        super(errorResponse.getMessage());
        this.statusCode = errorResponse.getStatusCode();
    }
}
