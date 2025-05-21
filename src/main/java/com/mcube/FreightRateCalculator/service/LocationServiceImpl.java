package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.external.GeocodingApiClient;
import com.mcube.FreightRateCalculator.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {


    @Autowired
    private LocationRepository locationRepo;
    @Autowired
    private LocationAliasService locationAliasService;
    @Autowired
    private GeocodingApiClient geocodingApiClient;

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
                geocodingApiClient.fetchLocation(locationName);
                //get data from API
                return Optional.empty();
            }
        }
    }
}
