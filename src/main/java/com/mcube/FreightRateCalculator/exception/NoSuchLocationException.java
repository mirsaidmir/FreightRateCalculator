package com.mcube.FreightRateCalculator.exception;

public class NoSuchLocationException extends RuntimeException {
    public NoSuchLocationException(String message) {
        super(message);
    }
}
