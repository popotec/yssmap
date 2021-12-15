package com.broadenway.ureasolution.api;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.broadenway.ureasolution.dto.GasStationDto;
import com.broadenway.ureasolution.service.GasStationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/stations")
@RequiredArgsConstructor
public class GasStationApi {

	private final GasStationService gasStationService;

	@Value("${kakao-map-key}")
	private String kakaoMapKey;

	@GetMapping
	public ResponseEntity<List<GasStationDto>> getStations() {
		List<GasStationDto> gasStationDatas = gasStationService.findAllDtos();
		return ResponseEntity.ok(gasStationDatas);
	}

	@PostMapping
	public ResponseEntity<GasStationDto> createGasStation(@RequestBody GasStationDto gasStationDto) {
		GasStationDto createdGasStation = gasStationService.save(gasStationDto);
		return ResponseEntity.created(URI.create("/api/stations/" + createdGasStation.getStationCode())).build();
	}

	@GetMapping("/optimized")
	public ResponseEntity<List<GasStationDto>> getStationsViewOptimized(@RequestParam(value = "latitude") String latitude,
		@RequestParam(value = "longitude") String longitude) {

		List<GasStationDto> gasStationDatas = gasStationService.findAllDtosOptimizedDto(latitude, longitude);
		return ResponseEntity.ok(gasStationDatas);
	}
}
