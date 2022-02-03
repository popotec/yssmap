package yssmap.batch.job.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.RequiredArgsConstructor;
import yssmap.stationapi.service.GasStationAPIService;

@RequiredArgsConstructor
public class DeleteInvalidStationTasklet implements Tasklet {

	private final GasStationAPIService storeGasStationAPIService;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// storeGasStationAPIService
			return RepeatStatus.FINISHED;
	}
}
