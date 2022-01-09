package yssmap.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import yssmap.main.dto.GasStationDto;
import yssmap.main.service.GasStationService;

import lombok.RequiredArgsConstructor;

@Controller()
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class GasStationController {

	private final GasStationService gasStationService;

	@Value("${kakao-map-key:}")
	private String kakaoMapKey;

	@RequestMapping("/stations")
	public ModelAndView getStations() {
		List<GasStationDto> gasStationData = gasStationService.findAllDtos();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("gasStations", gasStationData);
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@RequestMapping("/")
	public ModelAndView getStationsView() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mapKey", kakaoMapKey);
		modelAndView.setViewName("stations-view");
		return modelAndView;
	}
}
