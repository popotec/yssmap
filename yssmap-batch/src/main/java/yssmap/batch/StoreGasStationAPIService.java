package yssmap.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;
import yssmap.main.dto.GasStationDto;

@Service
@Transactional
public class StoreGasStationAPIService{

	private static final String BASE_REQUEST_URL = "https://api.odcloud.kr/api/uws/v1/inventory";
	private static final int INIT_PAGE = 1;
	private static final int INIT_PER_PAGE = 200;

	@Value("${dataportal.servicekey}")
	private String serviceKey;

	@Autowired
	private final GasStationRepository gasStationRepository;

	public StoreGasStationAPIService(GasStationRepository gasStationRepository) {
		this.gasStationRepository = gasStationRepository;
	}

	@CacheEvict(value = "station", allEntries = true)
	public void fetchGasStationData() {
		List<GasStationDto> gasStations = getStationsFromAPI();
		storeDatabase(gasStations);
	}

	private void storeDatabase(List<GasStationDto> gasStations) {
		for (GasStationDto gasStation : gasStations) {
			Optional<GasStation> findGasStation = gasStationRepository.findByStationCode(
				gasStation.getStationCode());
			GasStation requestGasStation = gasStation.toGasStation();
			if (findGasStation.isPresent()) {
				findGasStation.get().update(requestGasStation);
				continue;
			}
			gasStationRepository.save(requestGasStation);
		}
	}

	private List<GasStationDto> getStationsFromAPI() {

		int page = INIT_PAGE;

		Map result = requestAPICall(page);
		int totalCount = (Integer)result.get("totalCount");
		List<GasStationDto> gasStations = convertDataToGasStationEntity(result);

		if (totalCount<=INIT_PER_PAGE) {
			return gasStations;
		}

		int totalPage = (int)Math.ceil((double)totalCount / 200);
		for (int i = 2; i <= totalPage; i++) {
			Map nextResult = requestAPICall(i);
			gasStations.addAll(convertDataToGasStationEntity(nextResult));
		}

		return gasStations;
	}

	private Map requestAPICall(int page) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.AUTHORIZATION, serviceKey);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

		String urlBuilder = BASE_REQUEST_URL +
			"?page=" + page +
			"&perPage=" + INIT_PER_PAGE +
			"&serviceKey=" + serviceKey;

		System.out.println(urlBuilder);
		return restTemplate.exchange(urlBuilder, HttpMethod.GET, httpEntity, Map.class).getBody();
	}

	private List<GasStationDto> convertDataToGasStationEntity(Map responseBody) {
		List<Map> extractedData = (ArrayList)responseBody.get("data");

		return extractedData.stream().map(data ->
			new GasStationDto(
				(String)data.get(ResponseFieldName.STATION_CODE.getName()),
				(String)data.get(ResponseFieldName.NAME.getName()),
				(String)data.get(ResponseFieldName.ADDRESS.getName()),
				(String)data.get(ResponseFieldName.TEL_NO.getName()),
				(String)data.get(ResponseFieldName.OPENNING_HOURS.getName()),
				String.valueOf(data.get(ResponseFieldName.STOCSK.getName())),
				(String)data.get(ResponseFieldName.PRICE.getName()),
				Double.parseDouble((String)data.get(ResponseFieldName.LATITUDE.getName())),
				Double.parseDouble((String)data.get(ResponseFieldName.LONGITUDE.getName())),
				(String)data.get(ResponseFieldName.STD_DT.getName())
			)).collect(Collectors.toList());
	}
}
