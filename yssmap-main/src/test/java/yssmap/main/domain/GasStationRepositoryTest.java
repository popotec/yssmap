package yssmap.main.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GasStationRepositoryTest {

	@Autowired
	private GasStationRepository gasStationRepository;

	private GasStation gasStation;

	@BeforeEach
	public void setUp(){
		// given
		gasStation = new GasStation("K0011530",
			"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", 35.46050360,
			129.36478340, "2021-11-26 20:00:00");
	}

	@Test
	@DisplayName("stationCode로 조회")
	void findByStationCode(){
		//given
		GasStation savedGasStation = gasStationRepository.save(gasStation);

		//when
		Optional<GasStation> findGasStation = gasStationRepository.findById("K0011530");

		//then
		assertThat(findGasStation.isPresent()).isTrue();
		assertThat(findGasStation.orElse(null)).isEqualTo(savedGasStation);
	}

}
