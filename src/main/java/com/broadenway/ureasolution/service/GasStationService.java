package com.broadenway.ureasolution.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
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

	private static final double DEFAULT_LATITUDE = 37.3;
	private static final double DEFAULT_LONGITUDE = 126.5;
	private static final double POSITION_COVERAGE = 0.2;

	private final GasStationRepository gasStationRepository;

	public Optional<GasStation> findGasStationByStationCode(String stationCode) {
		return gasStationRepository.findById(stationCode);
	}

	@Transactional
	public GasStationDto save(GasStationDto gasStation) {
		return GasStationDto.from(gasStationRepository.save(gasStation.toGasStation()));
	}

	@Cacheable(value = "station")
	public List<GasStationDto> findAllDtos() {
		return findAll().stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	protected List<GasStation> findAll() {
		return gasStationRepository.findAll();
	}

	public List<GasStationDto> findAllNearCenterPositionDto(String latitude, String longitude) {
		return findAllDtosOptimized(latitude, longitude)
			.stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	public List<GasStation> findAllDtosOptimized(String latitude, String longitude) {
		validateBounds(latitude, longitude);
		double userInLatitude = Double.parseDouble(latitude);
		double userInLongitude = Double.parseDouble(longitude);

		// user position을 기준으로 위아래 양옆 +1 버퍼만큼 위치한 주유소만 조회
		// entity의 좌표 column 타입이 String
		return gasStationRepository.findAllInBoundary(String.valueOf(userInLatitude - POSITION_COVERAGE),
			String.valueOf(userInLatitude + POSITION_COVERAGE), String.valueOf(userInLongitude - POSITION_COVERAGE)
			, String.valueOf(userInLongitude + POSITION_COVERAGE));
	}

	public List<GasStationDto> findAllInMapBoundsDto(String westBound, String southBound, String eastBound,
		String northBound) {
		return findAllInMapBounds(westBound, southBound, eastBound, northBound)
			.stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	protected List<GasStation> findAllInMapBounds(String westBound, String southBound, String eastBound,
		String northBound) {
		validateBounds(westBound, eastBound, southBound, northBound);

		return gasStationRepository.findAllInBoundary(southBound, northBound
			, westBound, eastBound);
	}

	private void validateBounds(String... bounds) {
		for (String bound : bounds) {
			if (bound == null) {
				throw new IllegalArgumentException("맵 경계값을 포함하여 요청해야합니다.");
			}

			try {
				double position = Double.parseDouble(bound);
				if (position < 0) {
					throw new IllegalArgumentException("경계 좌표는 0 이상이어야 합니다.");
				}
			} catch (NumberFormatException ex) {
				throw new NumberFormatException("좌표는 숫자여야 합니다.");
			}
		}
	}
}
