package yssmap.batch.job.chunk;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import yssmap.stationapi.GasStationAPIService;
import yssmap.main.dto.GasStationDto;


public class GasStationApiWriter implements ItemWriter<List<GasStationDto>> {

	private final GasStationAPIService storeGasStationAPIService;


	public GasStationApiWriter(GasStationAPIService storeGasStationAPIService) {
		this.storeGasStationAPIService = storeGasStationAPIService;
	}

	@Override
	public void write(List<? extends List<GasStationDto>> items) throws Exception {
		items.stream()
			.forEach(gasStations -> storeGasStationAPIService.storeDatabase(gasStations));
	}
}
