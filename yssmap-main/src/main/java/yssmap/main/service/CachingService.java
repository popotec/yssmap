package yssmap.main.service;

import yssmap.main.domain.GasStation;
import yssmap.main.domain.GasStationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Cacheable(value = "station")
    public List<GasStation> findAll() {
        return gasStationRepository.findAllByDeletedAtIsNull();
    }

    public Page<GasStation> findAll(Pageable pageable) {
        return gasStationRepository.findAllByDeletedAtIsNull(pageable);

    }
}
