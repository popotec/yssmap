package yssmap.batch.job.chunk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import yssmap.stationapi.GasStationAPIService;
import yssmap.main.dto.GasStationDto;

public class GasStationApiReader implements ItemReader<List<GasStationDto>> {

	private static final Logger logger = LoggerFactory.getLogger("file");
	private final GasStationAPIService storeGasStationAPIService;
	private final int maxPage;
	private int page = 0; // TODO: ThreadLocal로 변경

	public GasStationApiReader(GasStationAPIService storeGasStationAPIService) {
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

		logger.info("fetch page = {}", page);
		List<GasStationDto> stationDtos = storeGasStationAPIService.fetchStations(page);
		logger.info("fetch data size =  {}", stationDtos.size());
		return stationDtos;
	}
}
