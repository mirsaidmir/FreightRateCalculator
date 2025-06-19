package com.mcube.FreightRateCalculator.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeocodingApiClientException.class)
    public ResponseEntity<ApiErrorResponse> handleGeocodingException(
            GeocodingApiClientException exception,
            HttpServletRequest request) {
        return buildResponse(exception.getMessage(), exception.getHttpStatus(), request);
    }

    @ExceptionHandler(NoSuchLocationException.class)
    public ResponseEntity<ApiErrorResponse> handleLocationException(
            NoSuchLocationException exception,
            HttpServletRequest request) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DistanceCalculationFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleDistanceException(
            DistanceCalculationFailedException exception,
            HttpServletRequest request) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleCommonException(Exception exception, HttpServletRequest request) {
        return buildResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(String message, HttpStatus status, HttpServletRequest request) {
        ApiErrorResponse data = new ApiErrorResponse();
        data.setStatusCode(status.value());
        data.setError(status.getReasonPhrase());
        data.setInfo(message);
        data.setPath(request.getRequestURI());
        data.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(data, status);
    }
}
