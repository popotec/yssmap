package com.broadenway.ureasolution.service;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.broadenway.ureasolution.api.AcceptanceTest;
import com.broadenway.ureasolution.dto.GasStationDto;

class GasStationServiceTest extends AcceptanceTest {

	@Autowired
	private GasStationService gasStationService;

	private GasStationDto gasStation1;
	private GasStationDto gasStation2;

	@BeforeEach
	public void setUp() {
		//given
		gasStation1 = new GasStationDto("K0011530",
			"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", "35.46050360",
			"129.36478340", "2021-11-26 20:00:00");
		gasStation2 = new GasStationDto("K0011531",
			"서울시 강남구", "서울시 강남구 62-7", "02-3432-3866",
			"09:00~18:00", "2500", "1500", "36.46050360",
			"129.36478340", "2021-11-26 20:00:00");
		gasStationService.save(gasStation1);
		gasStationService.save(gasStation2);
	}

	@Test
	@DisplayName("전체 목록 조회")
	void findAll() {
		//when
		List<GasStationDto> stations = gasStationService.findAllDtos();
		System.out.println(stations.size());
		//then
		Assertions.assertThat(stations.containsAll(Arrays.asList(gasStation1, gasStation2))).isTrue();
	}
}