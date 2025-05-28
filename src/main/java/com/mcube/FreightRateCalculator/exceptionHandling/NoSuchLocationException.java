package com.mcube.FreightRateCalculator.exceptionHandling;

public class NoSuchLocationException extends RuntimeException {
    public NoSuchLocationException(String message) {
        super(message);
    }
}
