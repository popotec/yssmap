package com.broadenway.utils.scheduler;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.broadenway.utils.job.DateJobParameter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreStationScheduler {

	private final Job job;  // tutorialJob
	private final JobLauncher jobLauncher;

	// 5초마다 실행
	@Scheduled(fixedDelay = 5 * 1000L)
	public void executeJob () {
		try {
			jobLauncher.run(
				job,
				new JobParametersBuilder()
					.addString("datetime", LocalDateTime.now().toString())
					.toJobParameters()  // job parameter 설정
			);
		} catch (JobExecutionException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	// private SchedulerFactory schedulerFactory;
	// private Scheduler scheduler;
	//
	//
	// @PostConstruct
	// public void start() throws SchedulerException {
	//
	// 	schedulerFactory = new StdSchedulerFactory();
	// 	scheduler = schedulerFactory.getScheduler();
	// 	scheduler.start();
	//
	// 	JobDetail job = JobBuilder.newJob(DateJobParameter.class).withIdentity("aCmaJob").build();
	// 	Trigger trigger = TriggerBuilder.newTrigger()
	// 		.withSchedule(CronScheduleBuilder.cronSchedule("30 * * * * ?"))
	// 		.build();
	//
	// 	scheduler.scheduleJob(job, trigger);
	// }
}
