package com.broadenway.ureasolution.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.broadenway.ureasolution.domain.GasStation;
import com.broadenway.ureasolution.dto.GasStationDto;
import com.broadenway.ureasolution.repository.GasStationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GasStationService {

	private final GasStationRepository gasStationRepository;

	public Optional<GasStation> findGasStationByStationCode(String stationCode){
		return gasStationRepository.findById(stationCode);
	}

	@Transactional
	public GasStationDto save(GasStationDto gasStation) {
		return GasStationDto.from(gasStationRepository.save(gasStation.toGasStation()));
	}

	public List<GasStationDto> findAllDtos() {
		return findAll().stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	public List<GasStation> findAll() {
		return gasStationRepository.findAll();
	}
}
