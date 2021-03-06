package com.juanedu.springbatchpoc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.builder.JmsItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.core.JmsTemplate;

@EnableBatchProcessing
@Configuration
public class DequeueAndWriteFileConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	private static final Logger log = LoggerFactory.getLogger(DequeueAndWriteFileConfig.class);
	
	@Bean
	public Step dequeueAndWriteFileStep() {
		return this.stepBuilderFactory.get("dequeueAndWriteFileStep")
				.<DomObjectOut, DomObjectOut>chunk(100)
				.reader(queueOutItemReader(null))
				.processor(writeItemProcessor())
				.writer(fileItemWriter(null))
				.build();
	}

	@Bean
	@StepScope
	public JmsItemReader<DomObjectOut> queueOutItemReader(JmsTemplate jmsTemplateOut) {

		return new JmsItemReaderBuilder<DomObjectOut>()
				.jmsTemplate(jmsTemplateOut)
				.itemType(DomObjectOut.class)
				.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<DomObjectOut, DomObjectOut> writeItemProcessor()
	{
		return item -> {
			log.debug(item.toString());
			return item;
		};
	}
	
	@Bean
	@StepScope
	public FlatFileItemWriter<DomObjectOut> fileItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile)
	{
		return new FlatFileItemWriterBuilder<DomObjectOut>()
				.name("fileItemWriter")
				.resource(outputFile)
				.delimited()
				.names(new String[] {"clave", "datos", "delay", "timestamp", "worker"})
				.build();
	}

}
