package yssmap.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import yssmap.batch.job.tasklet.DeleteInvalidStationTasklet;
import yssmap.main.service.GasStationService;
import yssmap.stationapi.service.GasStationAPIService;

@Configuration
@RequiredArgsConstructor
public class DeleteInvalidStationJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final GasStationService gasStationService;

	@Bean
	public Job deleteInvalidStationJob() {
		return jobBuilderFactory.get("deleteInvalidStationJob")
			.start(deleteInvalidStationStep())
			.build();
	}

	@Bean
	@JobScope
	public Step deleteInvalidStationStep() {
		return stepBuilderFactory.get("deleteInvalidStationStep")
			.tasklet(deleteOldStationTasklet())
			.build();
	}

	@Bean
	@StepScope
	public Tasklet deleteOldStationTasklet(){
		return new DeleteInvalidStationTasklet(gasStationService);
	}
}
