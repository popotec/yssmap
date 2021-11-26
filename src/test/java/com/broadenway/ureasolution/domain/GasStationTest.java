package com.broadenway.ureasolution.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GasStationTest {

	@Autowired
	EntityManager em;

	private GasStation gasStation = new GasStation("K0011530",
		"용인 수지구", "용인시 수지구 232-3", "031-324-2321",
		"09:00~18:00", "2500", "1500", "35.46050360",
		"129.36478340", "2021-11-26 20:00:00");

	@DisplayName("GasStation 생성")
	@Test
	void gasStation() {

		em.persist(gasStation);
		GasStation findStation = em.find(GasStation.class,gasStation.getStationCode());
		Assertions.assertThat(gasStation).isEqualTo(findStation);
	}

	@DisplayName("GasStation 생성")
	@Test
	void update() {
		String updateName="서울시 강남구";
		GasStation updateInfoGasStation = new GasStation("K0011530",
			updateName, "용인시 수지구 232-3", "031-324-2321",
			"09:00~18:00", "2500", "1500", "35.46050360",
			"129.36478340", "2021-11-26 20:00:00");

		em.persist(gasStation);
		gasStation.update(updateInfoGasStation);
		GasStation findStation = em.find(GasStation.class,gasStation.getStationCode());
		Assertions.assertThat(findStation.getName()).isEqualTo(updateName);
	}

}