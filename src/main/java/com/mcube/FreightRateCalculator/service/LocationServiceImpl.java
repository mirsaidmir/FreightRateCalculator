package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.dto.GeoResponseDto;
import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.entity.LocationAlias;
import com.mcube.FreightRateCalculator.external.GeocodingApiClient;
import com.mcube.FreightRateCalculator.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
        return locationRepo.findByNormalizedNameIgnoreCase(locationName)
                .or(()->locationAliasService.findLocation(locationName))
                .or(()->sendRequestAndUpdateLocationAndAlias(locationName));

                //ResponseEntity<GeoResponseDto> response = geocodingApiClient.fetchGeoResponseDto(locationName);
                //get data from API
                //return Optional.empty();
    }

    private Optional<Location> sendRequestAndUpdateLocationAndAlias(String locationName) {
        Optional<Location> optLocation = geocodingApiClient.getLocation(locationName);
        if (optLocation.isPresent()) {
            Location location = optLocation.get();
            location = saveAliasAndLocation(location, locationName);
            return Optional.of(location);
        }
        return Optional.empty();
    }

    @Transactional
    private Location saveAliasAndLocation(Location location, String alias) {
        location = locationRepo.save(location);
        locationAliasService.addAlias(location, alias);
        return location;
    }


}
