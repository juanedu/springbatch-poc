package com.juanedu.springbatchpoc.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableBatchProcessing
@Configuration
@Import({ ReadFileAndQueueConfig.class, ProcessQueueConfig.class })
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Bean
	public Job job(
			ReadFileAndQueueConfig stepOne,
			ProcessQueueConfig stepTwo,
			DequeueAndWriteFileConfig stepThree) {
		return this.jobBuilderFactory.get("JobPOC")
				.start(stepOne.readFileAndQueueStep())
				.next(stepTwo.processQueueStep())
				.next(stepThree.dequeueAndWriteFileStep())
				.build();
	}
}
