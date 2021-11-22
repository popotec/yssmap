package com.broadenway.ureasolution.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broadenway.ureasolution.service.GasStationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path= "/request/datapotal")
public class GasStationStoreAPI {

	private final GasStationService gasStationService;

}
