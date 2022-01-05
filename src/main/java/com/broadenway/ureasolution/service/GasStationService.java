package com.broadenway.ureasolution.service;

import com.broadenway.ureasolution.dto.MapBound;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.broadenway.ureasolution.domain.GasStation;
import com.broadenway.ureasolution.dto.GasStationDto;
import com.broadenway.ureasolution.repository.GasStationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GasStationService {

    private final GasStationRepository gasStationRepository;
    private final CachingService cachingService;

    @CacheEvict(value = "station", allEntries = true)
    @Transactional
    public GasStationDto save(GasStationDto gasStation) {
        return GasStationDto.from(gasStationRepository.save(gasStation.toGasStation()));
    }

    public List<GasStationDto> findAllDtos() {
        List<GasStation> stations = cachingService.findAll();
        return stations.stream()
            .map(GasStationDto::from)
            .collect(Collectors.toList());
    }

    public List<GasStationDto> findAllDtos(Pageable pageable) {
        Page<GasStation> stations = cachingService.findAll(pageable);
        return stations.stream()
            .map(GasStationDto::from)
            .collect(Collectors.toList());
    }

    public List<GasStationDto> findAllInMapBoundsDto(MapBound mapBound) {
        List<GasStation> stations = cachingService.findAll();
        return filterStationsInBounds(stations, mapBound);
    }

    protected List<GasStationDto> filterStationsInBounds(List<GasStation> stations, MapBound mapBound) {
        return stations.stream()
            .parallel()
            .filter(
                gasStation -> mapBound.isCover(gasStation.getLatitude(), gasStation.getLongitude()))
            .map(GasStationDto::from)
            .collect(Collectors.toList());
    }

    protected List<GasStation> findAllInMapBounds(String westBound, String southBound,
        String eastBound, String northBound) {
        validateBounds(westBound, eastBound, southBound, northBound);

        return gasStationRepository.findAllInBoundary(southBound, northBound
            , westBound, eastBound);
    }

    private void validateBounds(String... bounds) {
        for (String bound : bounds) {
            if (bound == null) {
                throw new IllegalArgumentException("맵 경계값을 포함하여 요청해야합니다.");
            }

            try {
                Double position = Double.parseDouble(bound);
                if (position < 0) {
                    throw new IllegalArgumentException("경계 좌표는 0 이상이어야 합니다.");
                }
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("좌표는 숫자여야 합니다.");
            }
        }
    }
}
