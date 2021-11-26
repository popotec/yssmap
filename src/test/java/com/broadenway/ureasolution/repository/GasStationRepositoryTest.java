package com.broadenway.ureasolution.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.broadenway.ureasolution.domain.GasStation;

@DataJpaTest
class GasStationRepositoryTest {

	@Autowired
	private GasStationRepository gasStationRepository;

	private GasStation gasStation = new GasStation("K0011530",
		"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
		"09:00~18:00", "2500", "1500", "35.46050360",
		"129.36478340", "2021-11-26 20:00:00");

	@Test
	@DisplayName("stationCode로 조회")
	void findByStationCode(){
		//given
		GasStation savedGasStation = gasStationRepository.save(gasStation);
		Optional<GasStation> findGasStation = gasStationRepository.findById("K0011530");
		assertThat(findGasStation.isPresent()).isTrue();
		assertThat(findGasStation.orElse(null)).isEqualTo(savedGasStation);

		// when

		// then

	}

}