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

	public List<GasStationDto> findAllDtos() {
		return findAll().stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	public List<GasStation> findAll() {
		return gasStationRepository.findAll();
	}

	public List<GasStationDto> findAllNearCenterPositionDto(String latitude, String longitude) {
		return findAllDtosOptimized(latitude, longitude)
			.stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());
	}

	public List<GasStation> findAllDtosOptimized(String latitude, String longitude) {
		double userInLatitude = getLatitude(latitude);
		double userInLongitude = getLongitude(longitude);

		// user position을 기준으로 위아래 양옆 +1 버퍼만큼 위치한 주유소만 조회
		// entity의 좌표 column 타입이 String
		return gasStationRepository.findAllCloseToUserPosition(String.valueOf(userInLatitude - POSITION_COVERAGE),
			String.valueOf(userInLatitude + POSITION_COVERAGE), String.valueOf(userInLongitude - POSITION_COVERAGE)
			, String.valueOf(userInLongitude + POSITION_COVERAGE));
	}

	private double getLatitude(String inputLatitude) {
		if (inputLatitude == null) {
			return DEFAULT_LATITUDE;
		}

		try {
			double latitude = Double.parseDouble(inputLatitude);
			if (isNotAcceptableLatitude(latitude)) {
				return DEFAULT_LATITUDE;
			}
			return latitude;
		} catch (NumberFormatException ex) {
			return DEFAULT_LATITUDE;
		}
	}

	private double getLongitude(String inputLongitude) {
		if (inputLongitude == null) {
			return DEFAULT_LONGITUDE;
		}

		try {
			double longitude = Double.parseDouble(inputLongitude);
			if (isNotAcceptableLongitude(longitude)) {
				return DEFAULT_LONGITUDE;
			}
			return longitude;
		} catch (NumberFormatException ex) {
			return DEFAULT_LONGITUDE;
		}
	}

	private boolean isNotAcceptableLatitude(Double latitude) {
		if (latitude == null) {
			return true;
		}
		if (latitude < 31 || latitude > 39) {
			return true;
		}
		return false;
	}

	private boolean isNotAcceptableLongitude(Double longitude) {
		if (longitude == null) {
			return true;
		}
		if (longitude < 123 || longitude > 130) {
			return true;
		}
		return false;
	}
}
