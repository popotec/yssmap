package yssmap.batch.job.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.RequiredArgsConstructor;
import yssmap.main.service.GasStationService;

@RequiredArgsConstructor
public class DeleteInvalidStationTasklet implements Tasklet {

	private final GasStationService gasStationService;
	private final static int ALIVE_PERIOD=14;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		gasStationService.deleteOldStations(ALIVE_PERIOD);
		return RepeatStatus.FINISHED;
	}
}
