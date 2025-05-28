package com.mcube.FreightRateCalculator.exceptionHandling;

public class DistanceCalculationFailedException extends RuntimeException {
    public DistanceCalculationFailedException(String message) {
        super(message);
    }
}
