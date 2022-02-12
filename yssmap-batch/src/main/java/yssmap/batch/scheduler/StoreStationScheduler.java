package yssmap.batch.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile({"prod","local"})
public class StoreStationScheduler {

	private static final Logger logger = LoggerFactory.getLogger("file");

	private final Job storeGasStationJob;
	private final Job deleteInvalidStationJob;
	private final JobLauncher jobLauncher;

	// 1시간 마다 실행
	@Scheduled(fixedDelay = 60 *60 * 1000L)
	public void executeStoreGasStationJob () {
		try {
			logger.info("fetch gas station job start");
			jobLauncher.run(
				storeGasStationJob,
				new JobParametersBuilder()
					.addString("datetime", LocalDateTime.now().toString())
					.toJobParameters()  // job parameter 설정
			);
			logger.info("successfully complete job\n\n");
		} catch (JobExecutionException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	// 매일 0시 1초 실행
	@Scheduled(cron = "1 0 0 * * *")
	public void executeDeleteOldStationsJob () {
		try {
			logger.info("delete old stations start");
			jobLauncher.run(
				deleteInvalidStationJob,
				new JobParametersBuilder()
					.addString("datetime", LocalDateTime.now().toString())
					.toJobParameters()  // job parameter 설정
			);
			logger.info("successfully complete job\n\n");
		} catch (JobExecutionException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
