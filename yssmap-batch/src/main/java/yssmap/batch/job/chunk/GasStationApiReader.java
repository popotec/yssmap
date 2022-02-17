package yssmap.batch.job.chunk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import yssmap.stationapi.service.GasStationAPIService;
import yssmap.main.dto.GasStationDto;

public class GasStationApiReader implements ItemReader<List<GasStationDto>> {

	private static final Logger logger = LoggerFactory.getLogger("file");
	private final GasStationAPIService storeGasStationAPIService;
	private final int maxPage;

	/*
	 * 멀티스레드로 변경할 경우 page에 대한 연산이 thread-safe 하지 않으므로 ItemReader -> SynchronizedItemStreamReaderBuilder 로
	 * 변경하여 사용해야 한다.
	 * 각 스레드별 page 번호를 다르게 저장하면 안되고, 같은 page 번호를 공유하며 동기화해서 사용해야 하므로 ThreadLocal은 사용하면 안된다.
 	 */
	private int page = 0;

	public GasStationApiReader(GasStationAPIService storeGasStationAPIService) {
		this.storeGasStationAPIService = storeGasStationAPIService;
		maxPage = storeGasStationAPIService.getTotalPage();
	}

	@Override
	public List<GasStationDto> read() {
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
