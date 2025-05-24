package com.mcube.FreightRateCalculator.repository;

import com.mcube.FreightRateCalculator.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByNormalizedNameIgnoreCase(String normalizedName);

    boolean existsByNormalizedNameIgnoreCase(String normalizedName);
}
