package yssmap.stationapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;
import yssmap.main.dto.GasStationDto;

@Service
@Transactional
public class GasStationAPIService {

	private static final String BASE_REQUEST_URL = "https://api.odcloud.kr/api/uws/v1/inventory";
	public static final int INIT_PAGE = 1;
	public static final int INIT_PER_PAGE = 200;

	@Value("${dataportal.servicekey}")
	private String serviceKey;

	private final GasStationRepository gasStationRepository;
	private final RestTemplate restTemplate;

	@Autowired
	public GasStationAPIService(GasStationRepository gasStationRepository,
		RestTemplateBuilder restTemplateBuilder) {
		this.gasStationRepository = gasStationRepository;
		this.restTemplate = initRestTemplate(restTemplateBuilder);
	}

	private RestTemplate initRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		HttpComponentsClientHttpRequestFactory factory = makeRequestFactory(makeHttpClient());
		return restTemplateBuilder.requestFactory(()->factory).build();
	}

	@CacheEvict(value = "station", allEntries = true)
	public void storeDatabase(List<GasStationDto> gasStations) {
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

	public List<GasStationDto> fetchStations(int page) {
		return fetchStations(page,INIT_PER_PAGE);
	}

	public List<GasStationDto> fetchStations(int page, int pageSize) {
		Map result = requestAPICall(page, pageSize);
		return convertDataToGasStationEntity(result);
	}

	public int getTotalPage() {
		return getTotalPage(INIT_PER_PAGE);
	}

	public int getTotalPage(int pageSize) {
		return (int)Math.ceil((double)getTotalCount(pageSize) / pageSize);
	}

	private int getTotalCount(int pageSize) {
		Map result = requestAPICall(INIT_PAGE, pageSize);
		return (Integer)result.get("totalCount");
	}

	private Map requestAPICall(int page, int pageSize) {
		String requestUrl = getApiPath(page, pageSize);
		System.out.println(requestUrl);
		return restTemplate.exchange(requestUrl, HttpMethod.GET, getHeaders(), Map.class).getBody();
	}

	private HttpComponentsClientHttpRequestFactory makeRequestFactory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(10 * 1000);
		factory.setReadTimeout(10 * 1000);
		return factory;
	}

	private HttpClient makeHttpClient() {
		return HttpClientBuilder.create()
			.setMaxConnTotal(100)
			.setMaxConnPerRoute(1) //멀티 스레드로 돌리면 스레드 갯수만큼 할당하면 될 것 같다.
			.build();
	}

	private HttpEntity<?> getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.AUTHORIZATION, serviceKey);
		return new HttpEntity<>(httpHeaders);
	}

	private String getApiPath(int page, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(BASE_REQUEST_URL)
			.append("?page=").append(page)
			.append("&perPage=").append(pageSize)
			.append("&serviceKey=").append(serviceKey);
		return sb.toString();
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
