package com.mcube.FreightRateCalculator.exceptionHandling;

import com.mcube.FreightRateCalculator.dto.ErrorGeoResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GeocodingApiClientException extends RuntimeException {
    private final HttpStatus httpStatus;
    public GeocodingApiClientException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GeocodingApiClientException(ErrorGeoResponseDto errorResponse) {
        super(errorResponse.getMessage());
        this.httpStatus = errorResponse.getStatusCode() != 0
                ? HttpStatus.valueOf(errorResponse.getStatusCode())
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
