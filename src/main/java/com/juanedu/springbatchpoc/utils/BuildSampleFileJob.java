package com.juanedu.springbatchpoc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;

import com.juanedu.springbatchpoc.demo.DomObjectIn;

@EnableBatchProcessing
@SpringBootApplication
@ComponentScan("com.juanedu.springbatchpoc.utils")
public class BuildSampleFileJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private static final Logger log = LoggerFactory.getLogger(BuildSampleFileJob.class);
	
	@Bean
	public Job chunkBasedJob() {
		return this.jobBuilderFactory.get("buildSampleFileJob")
				.start(chunkStep())
				.build();
	}

	@Bean
	public Step chunkStep() {
		return this.stepBuilderFactory.get("chunkStep")
				.<DomObjectIn, DomObjectIn>chunk(500)
				.reader(bsfItemReader(100))
				.writer(bsfItemWriter(null))
				.build();
	}

	@StepScope
	@Bean
	public ListItemReader<DomObjectIn> bsfItemReader(
			@Value("#{jobParameters['maxItems']}") int maxItems) {
		
		Random r = new Random();
        
		List<DomObjectIn> items = new ArrayList<>(maxItems);
		for (int i = 0; i < maxItems; i++) {
			DomObjectIn dobj = new DomObjectIn();
			dobj.setClave(Integer.valueOf(r.nextInt((maxItems - 0) + 1)).toString());
			dobj.setDatos(UUID.randomUUID().toString());
			dobj.setDelay(r.nextInt((1000 - 0) + 1));
			items.add(dobj);
		}
		log.info("Se generaron " + items.size() + " registros");
		
		return new ListItemReader<>(items);
	}
	
	@StepScope
	@Bean
	public FlatFileItemWriter<DomObjectIn> bsfItemWriter(
			@Value("#{jobParameters['outputFile']}") Resource outputFile) {
		
		return new FlatFileItemWriterBuilder<DomObjectIn>()
				.name("bsfItemWriter")
				.resource(outputFile)
				.delimited()
				.names(new String[] {"clave", "datos", "delay"})
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(BuildSampleFileJob.class, args);
	}
}
