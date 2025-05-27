package com.mcube.FreightRateCalculator.service;

import com.mcube.FreightRateCalculator.entity.Distance;
import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.external.TomtomClient;
import com.mcube.FreightRateCalculator.repository.DistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class DistanceServiceImpl implements DistanceService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private DistanceRepository distanceRepo;

    @Autowired
    private TomtomClient routingClient;



    @Override
    @Transactional
    public void add(Location origin, Location destination, BigDecimal routeDistance) {
        if (distanceRepo.findByOriginAndDestination(origin,destination).isEmpty()) {
            Distance dist = new Distance();
            dist.setOrigin(origin);
            dist.setDestination(destination);
            dist.setDistance(routeDistance);
            if (dist.getFetchDate() == null) dist.setFetchDate(LocalDate.now());
            distanceRepo.save(dist);
        }
    }

    @Override
    public Optional<Distance> find(String strOrigin, String strDestination) {
        Location origin = locationService.findLocation(strOrigin).orElse(null);
        Location destination = locationService.findLocation(strDestination).orElse(null);
        Optional<Distance> dist = Optional.empty();
        if (origin != null && destination != null) {
            dist = distanceRepo.findByOriginAndDestination(origin, destination);
            if (dist.isEmpty()) {
                dist = sendRequestAndUpdate(origin, destination);
            }
        }

        return dist;
    }

    private Optional<Distance> sendRequestAndUpdate(Location origin, Location destination) {
        Optional<Distance> reqResult = routingClient.fetchDistance(origin, destination);
        if (reqResult.isPresent()) {
            distanceRepo.save(reqResult.get());
        }
        return reqResult;
    }
}
