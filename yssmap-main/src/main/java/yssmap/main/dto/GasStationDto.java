package yssmap.main.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import yssmap.main.domain.GasStation;

import lombok.Data;

@Data
public class GasStationDto {
	private String stationCode;
	private String name;
	private String address;
	private String telNo;
	private String openingHours;
	private String stocks;
	private String prices;
	private Double latitude;
	private Double longitude;
	private LocalDateTime lastModfeDttm;

	public GasStationDto() {
	}

	public GasStationDto(String stationCode, String name, String address, String telNo, String openingHours,
		String stocks, String prices, Double latitude, Double longitude, LocalDateTime lastModfeDttm) {
		this.stationCode = stationCode;
		this.name = name;
		this.address = address;
		this.telNo = telNo;
		this.openingHours = openingHours;
		this.stocks = stocks;
		this.prices = prices;
		this.latitude = latitude;
		this.longitude = longitude;
		this.lastModfeDttm = lastModfeDttm;
	}

	public static GasStationDto from(GasStation gasStation) {
		return new GasStationDto(gasStation.getStationCode(), gasStation.getName(), gasStation.getAddress(),
			gasStation.getTelNo(), gasStation.getOpeningHours(), gasStation.getStocks(), gasStation.getPrices(),
			gasStation.getLatitude(), gasStation.getLongitude(), gasStation.getLastModfeDttm());
	}

	public static List<GasStation> toGasStationList(List<GasStationDto> gasStationDtos) {
		return gasStationDtos.stream()
			.map(GasStationDto::toGasStation)
			.collect(Collectors.toList());
	}

	public GasStation toGasStation() {
		return new GasStation(stationCode, name, address, telNo, openingHours, stocks, prices, latitude,
			longitude, lastModfeDttm);
	}
}
