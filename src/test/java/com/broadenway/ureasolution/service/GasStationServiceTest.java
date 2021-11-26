package com.broadenway.ureasolution.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.broadenway.ureasolution.domain.GasStation;

@SpringBootTest
class GasStationServiceTest {

	@Autowired
	private GasStationService gasStationService;

	private GasStation gasStation1 = new GasStation("K0011530",
		"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
		"09:00~18:00", "2500", "1500", "35.46050360",
		"129.36478340", "2021-11-26 20:00:00");

	private GasStation gasStation2 = new GasStation("K0011531",
		"서울시 강남구", "서울시 강남구 62-7", "02-3432-3866",
		"09:00~18:00", "2500", "1500", "36.46050360",
		"129.36478340", "2021-11-26 20:00:00");

	@BeforeEach
	private void saveStations(){
		gasStationService.save(gasStation1);
		gasStationService.save(gasStation2);
	}

	@Test
	@DisplayName("전체 목록 조회")
	void findAll(){
		List<GasStation> stations = gasStationService.findAll();
		Assertions.assertThat(stations.containsAll(Arrays.asList(gasStation1,gasStation2))).isTrue();
	}

}