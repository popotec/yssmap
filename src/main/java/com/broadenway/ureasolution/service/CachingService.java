package com.broadenway.ureasolution.service;

import com.broadenway.ureasolution.domain.GasStation;
import com.broadenway.ureasolution.repository.GasStationRepository;
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
        return gasStationRepository.findAll();
    }

    public Page<GasStation> findAll(Pageable pageable) {
        return gasStationRepository.findAll(pageable);

    }
}
