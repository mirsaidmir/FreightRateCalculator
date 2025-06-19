package com.mcube.FreightRateCalculator.exception;

public class DistanceCalculationFailedException extends RuntimeException {
    public DistanceCalculationFailedException(String message) {
        super(message);
    }
}
