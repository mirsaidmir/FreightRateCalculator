package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.entity.LocationAlias;
import com.mcube.FreightRateCalculator.repository.LocationAliasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LocationAliasServiceImpl implements LocationAliasService {
    @Autowired
    private LocationAliasRepository locationAliasRepo;
    @Override
    public Optional<Location> findLocation(String alias) {
        return locationAliasRepo.findByAliasIgnoreCase(alias)
                .map(LocationAlias::getLocation);

    }

    @Transactional
    @Override
    public void addAlias(Location location, String alias) {
        boolean exists = locationAliasRepo.existsByAliasIgnoreCase(alias);
        if(!exists) {
            LocationAlias locationAlias = new LocationAlias();
            locationAlias.setAlias(alias);
            locationAlias.setLocation(location);
            locationAliasRepo.save(locationAlias);
        }
    }
}