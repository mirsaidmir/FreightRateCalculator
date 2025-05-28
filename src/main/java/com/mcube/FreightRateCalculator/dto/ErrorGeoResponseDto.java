package com.mcube.FreightRateCalculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ErrorGeoResponseDto {
    private HttpStatusCode statusCode;
    private String error;
    private String message;
}
