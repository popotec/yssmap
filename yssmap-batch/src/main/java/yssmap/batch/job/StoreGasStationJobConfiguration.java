package yssmap.batch.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import yssmap.stationapi.service.GasStationAPIService;
import yssmap.batch.job.chunk.GasStationApiReader;
import yssmap.batch.job.chunk.GasStationApiWriter;
import yssmap.main.dto.GasStationDto;
import yssmap.stationapi.service.GasStationStoreService;

@RequiredArgsConstructor
@Configuration
public class StoreGasStationJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final GasStationAPIService gasStationAPIService;
	private final GasStationStoreService gasStationStoreService;

	@Bean
	public Job storeGasStationJob() {
		return jobBuilderFactory.get("storeGasStationJob")
			.start(storeGasStationStep())
			.build();
	}

	@Bean
	@JobScope
	public Step storeGasStationStep() {
		return stepBuilderFactory.get("storeGasStationStep")
			.<List<GasStationDto>,List<GasStationDto>>chunk(1)
			.reader(gasStationApiReader())
			.writer(gasStationApiWriter())
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<List<GasStationDto>> gasStationApiReader() {
		return new GasStationApiReader(gasStationAPIService);
	}

	@Bean
	@StepScope
	public ItemWriter<List<GasStationDto>> gasStationApiWriter() {
		return new GasStationApiWriter(gasStationStoreService);
	}
}
