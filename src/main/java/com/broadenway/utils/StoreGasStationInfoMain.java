package com.broadenway.utils;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class StoreGasStationInfoMain {

	public static void main(String[] args) {
		new SpringApplicationBuilder(StoreGasStationInfoMain.class).web(WebApplicationType.NONE).run(args);
	}
}
