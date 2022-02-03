package yssmap.main.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GasStationRepositoryTest {

	@Autowired
	private GasStationRepository gasStationRepository;

	private GasStation gasStation;

	@BeforeEach
	public void setUp() {
		// given
		gasStation = new GasStation("K0011530",
			"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", 35.46050360,
			129.36478340, LocalDateTime.parse("2021-11-26T20:00:00"));
	}

	@Test
	@DisplayName("stationCode로 조회")
	void findByStationCodeAndDeletedAtIsNull() {
		//given
		GasStation savedGasStation = gasStationRepository.save(gasStation);

		//when
		Optional<GasStation> findGasStation = gasStationRepository.findByStationCodeAndDeletedAtIsNull("K0011530");

		//then
		assertThat(findGasStation.isPresent()).isTrue();
		assertThat(findGasStation.orElse(null)).isEqualTo(savedGasStation);
	}

	@Test
	@DisplayName("바운더리 내의 주유소 조회")
	void findAllInBoundary() {
		//given
		gasStationRepository.save(gasStation);

		//when
		List<GasStation> allInBoundary = gasStationRepository.findAllInBoundary(35.12, 37.232,
			125.234, 129.434);

		System.out.println(allInBoundary.size());

		//then
		Assertions.assertThat(allInBoundary).isNotEmpty();
	}

	@Test
	@DisplayName("삭제되지 않은 전체 주유소 조회")
	void findAllByDeletedAtIsNull() {
		//given
		gasStation.delete();
		gasStationRepository.save(gasStation);
		GasStation gasStation2 = new GasStation("K0011531",
			"용인 수지구2", "용인시 수지구2 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", 35.46050360,
			129.36478340, LocalDateTime.parse("2021-11-26T20:00:00"));
		gasStationRepository.save(gasStation2);

		//when
		List<GasStation> gasStations = gasStationRepository.findAllByDeletedAtIsNull();

		//then
		assertThat(gasStations.size()).isEqualTo(1);
		assertThat(gasStations.get(0)).isEqualTo(gasStation2);
	}

	@Test
	@DisplayName("유효하지 않은(오래된) 데이터 삭제일시 업데이트")
	void deleteOldStations() {
		//given
		String string = LocalDateTime.now().toString();
		System.out.println(string);
		// GasStation savedGasStation = gasStationRepository.save(gasStation);
		// GasStation savedGasStation2 = gasStationRepository.save(new GasStation("K0011531",
		// 	"용인 수지구2", "용인시 수지구2 232-3", "031-324-2321",
		// 	"09:00~18:00", "2500", "1500", 35.46050360,
		// 	129.36478340, localDateTime));
		//
		// int modifiedCount = gasStationRepository.deleteOldStations(localDateTime);
		//
		// //then
		// assertThat(modifiedCount).isEqualTo(1);
		//
		// //when
		// Optional<GasStation> findGasStation = gasStationRepository.findByStationCodeAndDeletedAtIsNull("K0011530");
		//
		// //then
		// assertThat(findGasStation.isPresent()).isTrue();
		// assertThat(findGasStation.orElse(null)).isEqualTo(savedGasStation);
	}
}
