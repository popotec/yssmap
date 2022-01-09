package yssmap.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import yssmap.batch.StoreGasStationAPIService;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StoreGasStationJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final StoreGasStationAPIService storeGasStationAPIService;

	// @Bean("jobParameter")
	// public DateJobParameter jobParameter(@Value("#{jobParameters[createDate]}") String createDateStr){
	// 	return new DateJobParameter(createDateStr); // (1)
	// }


	@Bean
	public Job job() {
		return jobBuilderFactory.get("storeGasStationJob")
			.start(step(null))
			.build();
	}

	@Bean
	@JobScope
	public Step step(@Value("#{jobParameters[datetime]}") String requestDate) {
		return stepBuilderFactory.get("step")
			.tasklet(storeTask(null))
			.build();
	}

	@Bean
	@StepScope
	public Tasklet storeTask(@Value("#{jobParameters[datetime]}") String requestDate){
		return (contribution, chunkContext) -> {
			log.info(">>>>> 배치 수행 시작");
			storeGasStationAPIService.fetchGasStationData();
			log.info(">>>>> 배치 수행 종료");
			return RepeatStatus.FINISHED;
		};
	}
}
