package yssmap.stationapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import yssmap.main.domain.GasStationRepository;
import yssmap.main.dto.GasStationDto;

@RestClientTest(value = {GasStationAPIService.class})
class StoreGasStationAPIServiceTest {

	@Autowired
	private GasStationAPIService gasStationAPIService;

	@MockBean
	private GasStationRepository gasStationRepository;

	@Autowired
	private MockRestServiceServer mockServer;

	@DisplayName("전체 페이지 갯수")
	@Test
	void getTotalPage() {
		//given
		String requestUrl = "https://api.odcloud.kr/api/uws/v1/inventory?page=1&perPage=3&serviceKey=OPnntQNdcjd1m2Dekuq/1FYtDBm8I6Y9kc2m88ol10qpE5BvY31fWpG7dqJSVS1cryrz/u1nNPWt7S6tWL3cwA%3D%3D";
		String expect = getExpectResult();
		mockServer.expect(requestTo(requestUrl))
			.andRespond(withSuccess(expect, MediaType.APPLICATION_JSON));

		//when
		int totalPage = gasStationAPIService.getTotalPage(3);

		//then => (총갯수)2637 / 3 = 879
		assertThat(totalPage).isEqualTo(879);
	}

	@DisplayName("api 호출해서 Dto로 변환")
	@Test
	void fetchStations() {
		//given
		String requestUrl = "https://api.odcloud.kr/api/uws/v1/inventory?page=1&perPage=3&serviceKey=OPnntQNdcjd1m2Dekuq/1FYtDBm8I6Y9kc2m88ol10qpE5BvY31fWpG7dqJSVS1cryrz/u1nNPWt7S6tWL3cwA%3D%3D";
		String expect = getExpectResult();
		mockServer.expect(requestTo(requestUrl))
			.andRespond(withSuccess(expect, MediaType.APPLICATION_JSON));

		//when
		List<GasStationDto> stationDtos = gasStationAPIService.fetchStations(1, 3);

		//then
		assertThat(stationDtos.size()).isEqualTo(3);
		조회_결과_확인(stationDtos);
	}

	private void 조회_결과_확인(List<GasStationDto> stationDtos) {
		List<String> stationCodes = stationDtos.stream()
			.map(GasStationDto::getStationCode)
			.collect(Collectors.toList());
		assertThat(stationCodes).containsAll(Arrays.asList("A0000004", "A0000012","A0000014"));
	}

	private String getExpectResult() {
		return "{\"currentCount\":3,\"data\":"
			+ "[{\"addr\":\"경기 광명시 오리로 608 (소하동)\",\"code\":\"A0000004\",\"color\":\"GREEN\",\"inventory\":\"1230\","
				+ "\"lat\":\"37.45620108\",\"lng\":\"126.88040650\",\"name\":\"(주)지에스이앤알 직영 하안단지주유소\","
				+ "\"openTime\":null,\"price\":\"2000\",\"regDt\":\"2022-01-27 14:13:59\",\"tel\":\"02-899-5202\"},"
			+ "{\"addr\":\"경기 용인시 기흥구 흥덕중앙로 92\",\"code\":\"A0000012\",\"color\":\"GREEN\",\"inventory\":\"1986\","
				+ "\"lat\":\"37.27596709\",\"lng\":\"127.07715021\",\"name\":\"(주)지에스이앤알 흥덕주유소\",\"openTime\":null,"
				+ "\"price\":\"1700\",\"regDt\":\"2022-01-27 06:03:09\",\"tel\":\"031-215-8053\"},"
			+ "{\"addr\":\"강원 인제군 북면 설악로 2775\",\"code\":\"A0000014\",\"color\":\"YELLOW\",\"inventory\":\"520\","
				+ "\"lat\":\"38.11466190\",\"lng\":\"128.20185550\",\"name\":\"지에스이앤알 직영 동일주유소\",\"openTime\":null,"
				+ "\"price\":\"1700\",\"regDt\":\"2022-01-27 05:31:58\",\"tel\":\"033-462-5124\"}],"
			+ "\"matchCount\":2637,\"page\":1,\"perPage\":3,\"totalCount\":2637}";
	}
}
