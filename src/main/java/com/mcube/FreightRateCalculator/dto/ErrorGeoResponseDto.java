package com.mcube.FreightRateCalculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorGeoResponseDto {
    private int statusCode;
    private String error;
    private String message;
}
