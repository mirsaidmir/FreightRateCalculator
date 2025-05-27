package com.mcube.FreightRateCalculator.repository;

import com.mcube.FreightRateCalculator.entity.Distance;
import com.mcube.FreightRateCalculator.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistanceRepository extends JpaRepository<Distance, Long> {
    Optional<Distance> findByOriginAndDestination(Location origin, Location destination);
}
