package com.broadenway.utils;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = "com.broadenway")
public class StoreGasStationInfoMain {

	public static void main(String[] args) {
		new SpringApplicationBuilder(StoreGasStationInfoMain.class).web(WebApplicationType.NONE).run(args);
	}
}
