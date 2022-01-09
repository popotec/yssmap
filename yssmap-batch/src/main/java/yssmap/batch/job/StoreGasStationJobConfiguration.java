package yssmap.batch.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yssmap.batch.StoreGasStationAPIService;
import yssmap.batch.job.chunk.GasStationApiReader;
import yssmap.batch.job.chunk.GasStationApiWriter;
import yssmap.main.dto.GasStationDto;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StoreGasStationJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final StoreGasStationAPIService storeGasStationAPIService;

	@Bean
	public Job job() {
		return jobBuilderFactory.get("storeGasStationJob")
			.start(step())
			.build();
	}

	@Bean
	@JobScope
	public Step step() {
		return stepBuilderFactory.get("step")
			.<List<GasStationDto>,List<GasStationDto>>chunk(1)
			.reader(gasStationApiReader())
			.writer(gasStationApiWriter())
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<List<GasStationDto>> gasStationApiReader() {
		return new GasStationApiReader(storeGasStationAPIService);
	}

	@Bean
	@StepScope
	public ItemWriter<List<GasStationDto>> gasStationApiWriter() {
		return new GasStationApiWriter(storeGasStationAPIService);
	}
}
