package yssmap.stationapi.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;
import yssmap.main.dto.GasStationDto;
import yssmap.stationapi.domain.ApiFetchResult;

@Service
@Transactional(readOnly = true)
public class GasStationStoreService {

	private static final Logger logger = LoggerFactory.getLogger("file");

	private final GasStationRepository gasStationRepository;

	private ThreadLocal<ApiFetchResult> apiFetchResultStore = new ThreadLocal<>();

	@Autowired
	public GasStationStoreService(GasStationRepository gasStationRepository) {
		this.gasStationRepository = gasStationRepository;
	}

	@Transactional
	@CacheEvict(value = "station", allEntries = true)
	public void storeGasStations(List<GasStationDto> gasStations) {
		initApiFetchResult();
		for (GasStationDto gasStationDto : gasStations) {
			storeGasStation(gasStationDto);
		}
		recordFetchResult();
		releaseResult();
	}

	private void initApiFetchResult() {
		apiFetchResultStore.set(new ApiFetchResult());
	}

	private void releaseResult() {
		apiFetchResultStore.remove();
	}

	private void recordFetchResult() {
		ApiFetchResult apiFetchResult = apiFetchResultStore.get();
		logger.info(apiFetchResult.toString());
	}

	private void storeGasStation(GasStationDto gasStationDto) {
		GasStation requestGasStation = gasStationDto.toGasStation();
		Optional<GasStation> findGasStation = gasStationRepository.findByStationCode(
			gasStationDto.getStationCode());

		if (findGasStation.isEmpty()) {
			gasStationRepository.save(requestGasStation);
			increaseCountOfCreated();
			return;
		}

		GasStation storedGasStation = findGasStation.get();
		boolean isChanged = storedGasStation.update(requestGasStation);
		if (isChanged) {
			increaseCountOfChange();
			return;
		}
		increaseCountOfNotChanged();
	}

	private void increaseCountOfChange() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfChanged();
	}

	private void increaseCountOfNotChanged() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfNotChanged();
	}

	private void increaseCountOfCreated() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfCreated();
	}
}
