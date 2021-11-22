package com.broadenway.ureasolution.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.broadenway.ureasolution.domain.GasStation;
import com.broadenway.ureasolution.repository.GasStationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GasStationService {

	private final GasStationRepository gasStationRepository;

	public Optional<GasStation> findGasStationByStationCode(String stationCode){
		return gasStationRepository.findByStationCode(stationCode);
	}

	@Transactional
	public void save(GasStation gasStation) {
		gasStationRepository.save(gasStation);
	}

	public void update(GasStation gasStation) {

	}

	public List<GasStation> findAll() {
		return gasStationRepository.findAll();
	}
}
