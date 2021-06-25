package com.juanedu.springbatchpoc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.batch.item.jms.builder.JmsItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;

@EnableBatchProcessing
@Configuration
public class ReadFileAndQueueConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	private static final Logger log = LoggerFactory.getLogger(ReadFileAndQueueConfig.class);
	
	@Bean
	public Step readFileAndQueueStep() {
		return this.stepBuilderFactory.get("readFileAndQueueStep")
				.<DomObjectIn, DomObjectIn>chunk(100)
				.reader(fileItemReader(null))
				.processor(itemProcessor())
				.writer(queueInItemWriter(null))
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<DomObjectIn> fileItemReader(
			@Value("#{jobParameters['file']}") Resource inputFile)
	{
		return new FlatFileItemReaderBuilder<DomObjectIn>()
				.name("fileItemReader")
				.resource(inputFile)
				.delimited()
				.names(new String[] {"clave", "datos", "delay"})
				.targetType(DomObjectIn.class)
				.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<DomObjectIn, DomObjectIn> itemProcessor()
	{
		return item -> {
			log.debug(item.toString());
			return item;
		};
	}
	
	@Bean
	public JmsItemWriter<DomObjectIn> queueInItemWriter(JmsTemplate jmsTemplate) {

		return new JmsItemWriterBuilder<DomObjectIn>()
				.jmsTemplate(jmsTemplate)
				.build();
	}

}
