package com.broadenway.ureasolution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.broadenway.ureasolution.domain.GasStation;

public interface GasStationRepository extends JpaRepository<GasStation, String> {

	@Query("select g from GasStation g "+
	"where g.latitude >=:bottomLatitude and g.latitude<=:topLatitude "+
	"and g.longitude>=:leftLongitude and g.longitude<=:rightLongitude")
	List<GasStation> findAllInBoundary(@Param("bottomLatitude") String bottomLatitude,
		@Param("topLatitude") String topLatitude, @Param("leftLongitude") String leftLongitude,
		@Param("rightLongitude") String rightLongitude);

	Optional<GasStation> findByStationCode(String stationCode);
}
