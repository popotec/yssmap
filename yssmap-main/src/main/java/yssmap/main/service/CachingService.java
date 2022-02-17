package yssmap.main.service;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import yssmap.main.dto.GasStationDto;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CachingService {

    private final GasStationRepository gasStationRepository;

    public List<GasStation> findAll() {
        return gasStationRepository.findAllByDeletedAtIsNull();
    }

    @Transactional
    @Cacheable(value = "station")
    public List<GasStationDto> findAllDto() {
        return findAll().stream()
            .map(GasStationDto::from)
            .collect(Collectors.toList());
    }

    public Page<GasStation> findAll(Pageable pageable) {
        return gasStationRepository.findAllByDeletedAtIsNull(pageable);
    }
}
