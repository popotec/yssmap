package yssmap.batch.job.chunk;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import lombok.extern.slf4j.Slf4j;
import yssmap.batch.StoreGasStationAPIService;
import yssmap.main.dto.GasStationDto;

// @Component
// @StepScope

@Slf4j
public class GasStationApiReader implements ItemReader<List<GasStationDto>> {
	private final int maxPage;
	private int page = 0; // TODO: ThreadLocal로 변경
	private final StoreGasStationAPIService storeGasStationAPIService;

	public GasStationApiReader(StoreGasStationAPIService storeGasStationAPIService) {
		this.storeGasStationAPIService = storeGasStationAPIService;
		maxPage = storeGasStationAPIService.getTotalPage();
	}

	@Override
	public List<GasStationDto> read() throws Exception, UnexpectedInputException, ParseException,
		NonTransientResourceException {
		page++;
		if (page > maxPage) {
			return null;
		}

		List<GasStationDto> stationDtos = storeGasStationAPIService.fetchStations(page);
		log.info("fetch data {}", stationDtos.size());
		return stationDtos;
	}
}
