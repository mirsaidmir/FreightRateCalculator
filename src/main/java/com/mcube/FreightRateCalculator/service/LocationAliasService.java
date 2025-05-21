package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.entity.LocationAlias;

import java.util.Optional;

public interface LocationAliasService {
    Optional<Location> findLocation(String alias);
    void addAlias(Location location, String alias);
}
