package com.broadenway.utils.gasdomain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GasStationRepository extends JpaRepository<GasStation, Long> {

	Optional<GasStation> findByStationCode(String stationCode);
}
