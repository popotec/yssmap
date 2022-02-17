package yssmap.batch.job.chunk;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import yssmap.main.dto.GasStationDto;
import yssmap.stationapi.service.GasStationStoreService;

public class GasStationApiWriter implements ItemWriter<List<GasStationDto>> {

	private final GasStationStoreService gasStationService;

	public GasStationApiWriter(GasStationStoreService gasStationService) {
		this.gasStationService = gasStationService;
	}

	@Override
	public void write(List<? extends List<GasStationDto>> items) {
		items.stream()
			.forEach(gasStations -> gasStationService.storeGasStations(gasStations));
	}
}
