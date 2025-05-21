package com.mcube.FreightRateCalculator.repository;

import com.mcube.FreightRateCalculator.entity.LocationAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationAliasRepository extends JpaRepository<LocationAlias, Long> {
    Optional<LocationAlias> findByAliasIgnoreCase(String alias);
}
