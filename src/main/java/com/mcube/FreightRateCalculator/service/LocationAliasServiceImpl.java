package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.entity.LocationAlias;
import com.mcube.FreightRateCalculator.repository.LocationAliasRepository;

import java.util.Optional;

public class LocationAliasServiceImpl implements LocationAliasService {
    private LocationAliasRepository locationAliasRepo;
    @Override
    public Optional<Location> findLocation(String alias) {
        Optional<LocationAlias> optLocAlias = locationAliasRepo.findByAliasIgnoreCase(alias);
        if (optLocAlias.isPresent()) {
            return Optional.of(optLocAlias.get().getLocation());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addAlias(Location location, String alias) {

    }
}