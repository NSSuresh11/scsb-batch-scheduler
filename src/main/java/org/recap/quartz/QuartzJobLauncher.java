package org.recap.quartz;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.recap.ScsbCommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Getter
@Setter
public class QuartzJobLauncher extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobLauncher.class);

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    /**
     * Quartz scheduler calls this method on each scheduled run of a job.
     * It identifies the spring batch configured job using the job name and holds the job execution details.
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Job job = jobLocator.getJob(jobName);
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addLong("time", System.currentTimeMillis());
            JobExecution jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
            logger.info("{}_{} was completed successfully. Status : {}", job.getName(), jobExecution.getId(), jobExecution.getStatus());
        } catch (Exception exception) {
            logger.error(ScsbCommonConstants.LOG_ERROR, exception);
        }
    }
}
