package com.broadenway.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.broadenway.utils.gasdomain.GasStation;
import com.broadenway.utils.gasdomain.GasStationRepository;

@Service
@Transactional
public class StoreGasStationAPIService implements ApplicationRunner {

	private static final String BASE_REQUEST_URL = "https://api.odcloud.kr/api/15094782/v1/uddi:6b2017af-659d-437e-a549-c59788817675";

	@Value("${dataportal.servicekey}")
	private String serviceKey;

	@Autowired
	private final GasStationRepository gasStationRepository;

	public StoreGasStationAPIService(GasStationRepository gasStationRepository) {
		this.gasStationRepository = gasStationRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Map responseData = requestAPIData();
		List<GasStation> gasStations = convertDataToGasStationEntity(responseData);
		storeDatabase(gasStations);
	}

	private void storeDatabase(List<GasStation> gasStations) {
		for(GasStation gasStation : gasStations){
			Optional<GasStation> findGasStation = gasStationRepository.findByStationCode(
				gasStation.getStationCode());
			if(findGasStation.isPresent()){
				findGasStation.get().update(gasStation);
				continue;
			}
			gasStationRepository.save(gasStation);
		}
	}

	private Map requestAPIData() {

		RestTemplate restTemplate = new RestTemplate();

		String parameter = "?page=1&perPage=10&serviceKey=";
		String url = BASE_REQUEST_URL+parameter+serviceKey;

		System.out.println("servicekey");
		System.out.println(serviceKey);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.AUTHORIZATION, serviceKey);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

		String page="1";
		String perPage="300";
		String urlBuilder = BASE_REQUEST_URL+"?page="+page+"&perPage="+perPage+"&serviceKey="+serviceKey;

		return restTemplate.exchange(urlBuilder, HttpMethod.GET, httpEntity, Map.class).getBody();
	}


	private List<GasStation> convertDataToGasStationEntity(Map responseBody) {
		List<Map> extractedData = (ArrayList)responseBody.get("data");

		return extractedData.stream().map(data ->
				new GasStation(
					(String)data.get(ResponseFieldName.STATION_CODE.getName()),
					(String)data.get(ResponseFieldName.NAME.getName()),
					(String)data.get(ResponseFieldName.ADDRESS.getName()),
					(String)data.get(ResponseFieldName.TEL_NO.getName()),
					(String)data.get(ResponseFieldName.OPENNING_HOURS.getName()),
					String.valueOf(data.get(ResponseFieldName.STOCSK.getName())),
					(String)data.get(ResponseFieldName.PRICE.getName()),
					(String)data.get(ResponseFieldName.LATITUDE.getName()),
					(String)data.get(ResponseFieldName.LONGITUDE.getName()),
					(String)data.get(ResponseFieldName.STD_DT.getName())
				)).collect(Collectors.toList());
	}
}
