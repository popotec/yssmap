package com.broadenway.ureasolution.api;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.broadenway.ureasolution.service.GasStationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path= "/request/datapotal")
public class GasStationStoreAPI {

	private final GasStationService gasStationService;


	@Value("${dataportal.servicekey}")
	private String serviceKey;

	private static final String BASE_REQUEST_URL = "https://api.odcloud.kr/api/15094782/v1/uddi:6b2017af-659d-437e-a549-c59788817675";

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getUreasolutionDatas() {

		RestTemplate restTemplate = new RestTemplate();

		String parameter = "?page=1&perPage=10&serviceKey=";
		String url = BASE_REQUEST_URL+parameter+serviceKey;

		System.out.println("servicekey");
		System.out.println(serviceKey);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_REQUEST_URL)
			.encode(Charset.forName("utf8"))
			.queryParam("page", 1)
			.queryParam("perPage", 300)
			.queryParam("serviceKey", serviceKey);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.AUTHORIZATION, serviceKey);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

		String page="1";
		String perPage="10";
		String urlBuilder = BASE_REQUEST_URL+"?page="+page+"&perPage="+perPage+"&serviceKey="+serviceKey;
		System.out.println("url");

		ResponseEntity<Map> responseEntity = restTemplate.exchange(urlBuilder, HttpMethod.GET, httpEntity, Map.class);
		System.out.println(responseEntity.getBody());
		// System.out.println(responseEntity.getBody());
		return ResponseEntity.ok().build();
	}

}
