package com.mcube.FreightRateCalculator.exceptionHandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeocodingApiClientException.class)
    public ResponseEntity<ApiErrorResponse> handleGeocodingException(GeocodingApiClientException exception, HttpServletRequest request) {
        return buildData(exception, exception.getStatusCode(), request);
    }

    @ExceptionHandler(NoSuchLocationException.class)
    public ResponseEntity<ApiErrorResponse> handleLocationException(NoSuchLocationException exception, HttpServletRequest request) {
        return buildData(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DistanceCalculationFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleDistanceException(DistanceCalculationFailedException exception, HttpServletRequest request) {
        return buildData(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleCommonException(Exception exception, HttpServletRequest request) {
        return buildData(exception, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiErrorResponse> buildData(Exception exception, HttpStatusCode httpStatusCode, HttpServletRequest request) {
        ApiErrorResponse data = new ApiErrorResponse();
        data.setStatusCode(httpStatusCode);
        data.setInfo(exception.getMessage());
        data.setPath(request.getRequestURI());
        data.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(data, httpStatusCode);
    }
}
