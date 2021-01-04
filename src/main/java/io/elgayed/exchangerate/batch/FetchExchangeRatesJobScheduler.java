package io.elgayed.exchangerate.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FetchExchangeRatesJobScheduler {
	
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	
	//Schedule fetching exchange rate job from european central bank to execute every day at 16:30;
	//the rates gets published at 16:00;
    @Scheduled(cron = "0 30 16 * * *", zone = "CET")
    public void perform() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters params = new JobParametersBuilder().addLong("JobId", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(job, params);
    }
}
