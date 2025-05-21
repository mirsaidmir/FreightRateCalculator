package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.repository.LocationRepository;

import java.util.Optional;

public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepo;
    private LocationAliasService locationAliasService;
    @Override
    public Optional<Location> findLocation(String locationName) {
        Optional<Location> optLocation = locationRepo.findByNormalizedNameIgnoreCase(locationName);
        if (optLocation.isPresent()) {
            return optLocation;
        } else {
            optLocation = locationAliasService.findLocation(locationName);
            if (optLocation.isPresent()) {
                return optLocation;
            } else {
                //get data from API
                return Optional.empty();
            }
        }
    }
}
