package com.broadenway.ureasolution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broadenway.ureasolution.domain.GasStation;

public interface GasStationRepository extends JpaRepository<GasStation, String> {

	// Optional<GasStation> findByStationCode(String stationCode);
}
