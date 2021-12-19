package com.broadenway.ureasolution.api;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:3052", allowedHeaders = "*")
// react 서버용 포트에 대해 cors 허용
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

	@GetMapping("/near-center")
	public ResponseEntity<List<GasStationDto>> getStationsNearCenterPosition(@RequestParam(value = "latitude") String latitude,
		@RequestParam(value = "longitude") String longitude) {

		List<GasStationDto> gasStationDatas = gasStationService.findAllNearCenterPositionDto(latitude, longitude);
		return ResponseEntity.ok(gasStationDatas);
	}

	@GetMapping("/bounds")
	public ResponseEntity<List<GasStationDto>> getStationsInMapBounds(@RequestParam(value = "westBound") String westBound,
		@RequestParam(value = "southBound") String southBound, @RequestParam(value = "eastBound") String eastBound,
		@RequestParam(value = "northBound") String northBound) {

		List<GasStationDto> gasStationDatas = gasStationService.findAllInMapBoundsDto(westBound, southBound,eastBound,northBound);
		return ResponseEntity.ok(gasStationDatas);
	}

}
