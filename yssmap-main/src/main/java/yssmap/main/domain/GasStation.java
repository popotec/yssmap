package yssmap.main.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class GasStation {

	// 주유소 코드
	@Id
	private String stationCode;

	// 주유소 이름
	private String name;

	// 주유소 주소
	private String address;

	//주유소 전화번호
	private String telNo;

	private String openingHours;

	private String stocks;

	private String prices;

	private Double latitude;

	private Double longitude;

	private String lastModfeDttm;

	private LocalDateTime deletedAt;

	protected GasStation() {
	}

	public GasStation(String stationCode, String name, String address, String telNo, String openingHours,
		String stocks, String prices, Double latitude, Double longitude, String lastModfeDttm) {
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

	public void update(GasStation updateInfo){
		this.name = updateInfo.name;
		this.address = updateInfo.address;
		this.telNo = updateInfo.telNo;
		this.openingHours = updateInfo.openingHours;
		this.stocks = updateInfo.stocks;
		this.prices = updateInfo.prices;
		this.latitude = updateInfo.latitude;
		this.longitude = updateInfo.longitude;
		this.lastModfeDttm = updateInfo.lastModfeDttm;
	}

	public void delete(){
		this.deletedAt=LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}

		if (!(o instanceof GasStation)){
			return false;
		}

		GasStation that = (GasStation)o;
		return Objects.equals(getStationCode(), that.getStationCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStationCode());
	}
}
