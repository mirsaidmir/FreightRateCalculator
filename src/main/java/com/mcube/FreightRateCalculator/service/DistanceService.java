package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Distance;
import com.mcube.FreightRateCalculator.entity.Location;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


public interface DistanceService {
    void add(Location origin, Location destination,  BigDecimal route);
    Optional<Distance> find(String origin, String destination);
}
