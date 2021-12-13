package com.broadenway.ureasolution.api;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.broadenway.ureasolution.dto.GasStationDto;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class GasStationApiTest extends AcceptanceTest {

	private static final String GAS_STATION_API_PATH="/api/stations";

	@DisplayName("주유소 목록 조회 테스트")
	@Test
	public void getStations() {
		// given
		ExtractableResponse<Response> 용서고속도로_상행_주유소 = 주유소_등록되어_있음("TEST1530",
			"용서고속도로(상행)", "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", "35.46050360",
			"129.36478340", "2021-11-26 20:00:00");

		ExtractableResponse<Response> 경부고속도로_하행_주유소 = 주유소_등록되어_있음("TEST1531",
			"경부고속도로(하행)", "충청북도 청주시 232-3", "043-213-2321",
			"09:00~18:00", "1500", "2500", "33.46050360",
			"125.36478340", "2021-11-27 20:00:00");

		// when
		ExtractableResponse<Response> 주유소_목록_조회됨 = 주유소_목록_조회_요청();

		// then
		요청_정상_처리_확인(주유소_목록_조회됨);
		주유소_목록_확인(Arrays.asList(용서고속도로_상행_주유소,경부고속도로_하행_주유소),주유소_목록_조회됨);
	}

	private void 요청_정상_처리_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주유소_목록_확인(List<ExtractableResponse<Response>> expectedResponses, ExtractableResponse<Response> responses) {
		List<String> expectedStationCodes = expectedResponses.stream()
			.map(it->it.header("Location").split("/")[3])
			.collect(Collectors.toList());

		List<String> resultStationCodes = responses.jsonPath().getList(".", GasStationDto.class).stream()
			.map(GasStationDto::getStationCode)
			.collect(Collectors.toList());
		assertThat(expectedStationCodes).containsAll(resultStationCodes);
	}

	private ExtractableResponse<Response> 주유소_등록되어_있음(String stationCode, String name, String address, String telNo, String openingHours,
		String stocks, String prices, String latitude, String longitude, String lastModfeDttm) {
		GasStationDto gasStationDto = new GasStationDto(stationCode, name, address, telNo, openingHours, stocks, prices,
			latitude, longitude, lastModfeDttm);

		return RestAssured
			.given().log().all()
			.body(gasStationDto)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(GAS_STATION_API_PATH)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주유소_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get(GAS_STATION_API_PATH)
			.then().log().all()
			.extract();
	}

}