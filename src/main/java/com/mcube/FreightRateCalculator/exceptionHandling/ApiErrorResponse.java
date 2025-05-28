package com.mcube.FreightRateCalculator.exceptionHandling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponse {
    private String info;
    private HttpStatusCode statusCode;
    private String path;
    private LocalDateTime timestamp;

}
