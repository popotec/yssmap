package yssmap.main.api;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import yssmap.main.dto.GasStationDto;

import org.springframework.transaction.annotation.Transactional;

@Transactional
class GasStationApiTest extends AcceptanceTest {

    private static final String GAS_STATION_API_PATH = "/api/stations";

    @DisplayName("주유소 목록 조회 테스트")
    @Test
    public void getStations() {
        // given
        ExtractableResponse<Response> 용서고속도로_상행_주유소 = 주유소_등록되어_있음("TEST1530",
            "용서고속도로(상행)", "용인시 수지구 232-3", "031-324-2321",
            "09:00~18:00", "2500", "1500", "35.46050360",
            "129.36478340", LocalDateTime.parse("2021-11-26T20:00:00"));

        ExtractableResponse<Response> 경부고속도로_하행_주유소 = 주유소_등록되어_있음("TEST1531",
            "경부고속도로(하행)", "충청북도 청주시 232-3", "043-213-2321",
            "09:00~18:00", "1500", "2500", "33.46050360",
            "125.36478340", LocalDateTime.parse("2021-11-27T20:00:00"));

        // when
        ExtractableResponse<Response> 주유소_목록_조회됨 = 주유소_목록_조회_요청();

        // then
        요청_정상_처리_확인(주유소_목록_조회됨);
        주유소_목록_확인(Arrays.asList(용서고속도로_상행_주유소, 경부고속도로_하행_주유소), 주유소_목록_조회됨);
    }

    @DisplayName("화면에 보이는 맵 바운더리 안의 좌표에 해당하는 주유소만 조회")
    @Test
    public void getStationsInMapBoundary() {
        // given
        ExtractableResponse<Response> 용서고속도로_상행_주유소 = 주유소_등록되어_있음("TEST1530",
            "용서고속도로(상행)", "용인시 수지구 232-3", "031-324-2321",
            "09:00~18:00", "2500", "1500", "38.46050360",
            "129.36478340", LocalDateTime.parse("2021-11-26T20:00:00"));

        ExtractableResponse<Response> 경부고속도로_하행_주유소 = 주유소_등록되어_있음("TEST1531",
            "경부고속도로(하행)", "충청북도 청주시 232-3", "043-213-2321",
            "09:00~18:00", "1500", "2500", "33.46050360",
            "125.36478340", LocalDateTime.parse("2021-11-27T20:00:00"));

        // when
        ExtractableResponse<Response> 주유소_목록_조회됨 = 맵_경계_내_주유소만_조회("123.23", "33.2",
            "125.45", "34.5234");

        // then
        요청_정상_처리_확인(주유소_목록_조회됨);
        주유소_목록_확인(Arrays.asList(경부고속도로_하행_주유소), 주유소_목록_조회됨);
    }

    private ExtractableResponse<Response> 맵_경계_내_주유소만_조회(String westBound, String southBound,
        String eastBound, String northBound) {
        return RestAssured
            .given()
            .log()
            .all()
            .when()
            .basePath(GAS_STATION_API_PATH)
            .get(
                "/bounds?" +
                    "westBound={westBound}&southBound={southBound}&eastBound={eastBound}&northBound={northBound}",
                westBound, southBound, eastBound, northBound)
            .then()
            .log()
            .all()
            .extract();
    }

    private void 요청_정상_처리_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주유소_목록_확인(List<ExtractableResponse<Response>> expectedResponses,
        ExtractableResponse<Response> responses) {
        List<String> expectedStationCodes = expectedResponses.stream()
            .map(it -> it.header("Location").split("/")[3])
            .collect(Collectors.toList());

        List<String> resultStationCodes = responses.jsonPath().getList(".", GasStationDto.class)
            .stream()
            .map(GasStationDto::getStationCode)
            .collect(Collectors.toList());

        assertThat(resultStationCodes).containsAll(expectedStationCodes);
    }

    private ExtractableResponse<Response> 주유소_등록되어_있음(String stationCode, String name,
        String address, String telNo, String openingHours, String stocks, String prices,
        String latitude, String longitude, LocalDateTime lastModfeDttm) {

        GasStationDto gasStationDto = new GasStationDto(stationCode, name, address, telNo,
            openingHours, stocks, prices,
            Double.parseDouble(latitude), Double.parseDouble(longitude), lastModfeDttm);

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
