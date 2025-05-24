package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;

import java.util.Optional;

public interface LocationService {
    /** returns Location if locationName in {normalizedName, aliases}
     * otherwise choose first option from API and save it
     * for locationName as alias for this Location
     * if there is no "similar city" returns "nullOptional"
     */
    Optional<Location> findLocation(String locationName);
    Optional<Location> findByNormalizedNameIgnoreCase(String locationName);

}
