package yssmap.main.service;

import static org.assertj.core.api.Assertions.assertThat;

import yssmap.main.domain.GasStation;
import yssmap.main.dto.MapBound;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import yssmap.main.dto.GasStationDto;

class GasStationServiceTest extends ServiceTest{

	@Autowired
	private GasStationService gasStationService;

	private GasStationDto gasStation1;
	private GasStationDto gasStation2;

	@BeforeEach
	public void setUp() {
		//given
		gasStation1 = new GasStationDto("K0011530",
			"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", 35.46050360,
			129.36478340, LocalDateTime.parse("2021-11-26T20:00:00"));

		gasStation2 = new GasStationDto("K0011531",
			"서울시 강남구", "서울시 강남구 62-7", "02-3432-3866",
			"09:00~18:00", "2500", "1500", 36.46050360,
			129.36478340, LocalDateTime.parse("2021-12-01T20:00:00"));

		gasStationService.save(gasStation1);
		gasStationService.save(gasStation2);
	}

	@Test
	@DisplayName("전체 목록 조회")
	void findAll() {
		//when
		List<GasStationDto> stations = gasStationService.findAllDtos();
		List<String> resultCodes = stations.stream()
			.map(GasStationDto::getStationCode)
			.collect(Collectors.toList());

		//then
		List<String> expectCodes = Arrays.asList(gasStation1.getStationCode(), gasStation2.getStationCode());
		assertThat(resultCodes.size()).isEqualTo(2);
		resultCodes.stream()
				.forEach(code -> assertThat(expectCodes.contains(code)));
	}

	@Test
	@DisplayName("전체 목록 조회")
	void findAllInMapBoundsDto() {
		//when
		String westBound= "126";
		String southBound= "34.0";
		String eastBound= "131";
		String northBound= "38.8";

		MapBound mapBound = MapBound.from(westBound,southBound,eastBound,northBound);
		List<GasStationDto> stations = gasStationService.findAllInMapBoundsDto(mapBound);
		List<String> resultCodes = stations.stream()
			.map(GasStationDto::getStationCode)
			.collect(Collectors.toList());

		//then
		List<String> expectCodes = Arrays.asList(gasStation1.getStationCode(), gasStation2.getStationCode());
		assertThat(resultCodes.size()).isEqualTo(2);
		resultCodes.stream()
			.forEach(code -> assertThat(expectCodes.contains(code)));
	}

	@Test
	void deleteOldStations(){
		//given
		LocalDate stdDate = LocalDate.parse("2021-11-30");

		//when
		gasStationService.deleteOldStations(stdDate,3);

		//then
		GasStation gasStation = gasStationService.findGasStation(gasStation1.getStationCode());
		assertThat(gasStation.getDeletedAt()).isNotNull();
	}
}
