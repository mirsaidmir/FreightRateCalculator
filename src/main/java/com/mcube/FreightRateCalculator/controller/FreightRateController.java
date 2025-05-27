package com.mcube.FreightRateCalculator.controller;

import com.mcube.FreightRateCalculator.entity.Distance;
import com.mcube.FreightRateCalculator.entity.Location;
import com.mcube.FreightRateCalculator.service.DistanceService;
import com.mcube.FreightRateCalculator.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/rates")
public class FreightRateController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private DistanceService distanceService;

    //@GetMapping(value = "/", produces = "application/json;charset=UTF-8")
    @GetMapping("/location")
    public Location findCity(@RequestParam String cityName) {
        Optional<Location> res = locationService.findLocation(cityName);
        return res.orElse(null);
    }

    @GetMapping("/distance")
    public Distance calculateRouteDistance(@RequestParam String origin,
                                           @RequestParam String destination) {
        Optional<Distance> res = distanceService.find(origin, destination);
        return res.orElse(null);
    }
}
