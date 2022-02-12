package yssmap.stationapi.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import yssmap.stationapi.exception.ApiResponseException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class StoreGasStationAPIServiceTest {

	@Autowired
	private GasStationAPIService gasStationAPIService;

	@DisplayName("유효하지 않은 요청 예외 테스트")
	@Test
	void apiResponseException_테스트() {
		//given
		String requestUrl = "https://api.odcloud.kr/api/uws/v1/inventory";

		//when, then
		Assertions.assertThatThrownBy(()->gasStationAPIService.callRestApi(requestUrl))
			.isInstanceOf(ApiResponseException.class);
	}

}
