package yssmap.main.service;

import yssmap.main.dto.MapBound;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yssmap.main.domain.GasStation;
import yssmap.main.dto.GasStationDto;
import yssmap.main.domain.GasStationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GasStationService {

    private final GasStationRepository gasStationRepository;
    private final CachingService cachingService;

    @Transactional
    @CacheEvict(value = "station", allEntries = true)
    public GasStationDto save(GasStationDto gasStation) {
        return GasStationDto.from(gasStationRepository.save(gasStation.toGasStation()));
    }

    public GasStation findGasStation(String stationCode){
        return gasStationRepository.findByStationCode(stationCode).orElseThrow(EntityNotFoundException::new);
    }

    public List<GasStationDto> findAllDtos() {
        return cachingService.findAllDto();
    }

    public List<GasStationDto> findAllDtos(Pageable pageable) {
        Page<GasStation> stations = cachingService.findAll(pageable);
        return stations.stream()
            .map(GasStationDto::from)
            .collect(Collectors.toList());
    }

    public List<GasStationDto> findAllInMapBoundsDto(MapBound mapBound) {
        List<GasStationDto> stations = cachingService.findAllDto();
        return filterStationsInBounds(stations, mapBound);
    }

    protected List<GasStationDto> filterStationsInBounds(List<GasStationDto> stations, MapBound mapBound) {
        return stations.stream()
            .parallel()
            .filter(
                gasStation -> mapBound.isCover(gasStation.getLatitude(), gasStation.getLongitude()))
            .collect(Collectors.toList());
    }

    public void deleteOldStations(LocalDate stdDate, int dayBefore){
        LocalDateTime lastValidDate = stdDate.minusDays(dayBefore).atStartOfDay();
        gasStationRepository.deleteOldStations(lastValidDate);
    }

    public void deleteOldStations(int dayBefore){
        deleteOldStations(LocalDateTime.now().toLocalDate(),dayBefore);
    }
}
