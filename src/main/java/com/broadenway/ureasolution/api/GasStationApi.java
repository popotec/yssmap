package com.broadenway.ureasolution.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.broadenway.ureasolution.domain.GasStationDto;
import com.broadenway.ureasolution.service.GasStationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class GasStationApi {

	private final GasStationService gasStationService;

	@Value("${kakao-map-key}")
	private String kakaoMapKey;

	@GetMapping("/stations")
	public ResponseEntity<List<GasStationDto>> getStations() {
		List<GasStationDto> gasStationDatas = gasStationService.findAll()
			.stream()
			.map(GasStationDto::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(gasStationDatas);
	}
}
