package com.devsuperior.githubuser.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

	@Bean
	public Job job(Step fetchUserDataAndStoreDBStep, JobRepository jobRepository) {
		return new JobBuilder("job", jobRepository)
				.start(fetchUserDataAndStoreDBStep)
				.incrementer(new RunIdIncrementer())
				.build();
	}
}
