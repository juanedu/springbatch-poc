package com.juanedu.springbatchpoc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.batch.item.jms.builder.JmsItemReaderBuilder;
import org.springframework.batch.item.jms.builder.JmsItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@EnableBatchIntegration
@Configuration
public class ProcessQueueConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	private static final Logger log = LoggerFactory.getLogger(ProcessQueueConfig.class);

	@Bean
	public Step processQueueStep() {
		return this.stepBuilderFactory.get("processQueueStep")
				.<DomObjectIn, DomObjectOut>chunk(100)
				.reader(queueItemReader(null))
				.writer(queueItemWriter(null))
				.build();
	}

	@Bean
	public JmsItemReader<DomObjectIn> queueItemReader(JmsTemplate jmsTemplate) {

		return new JmsItemReaderBuilder<DomObjectIn>().jmsTemplate(jmsTemplate).itemType(DomObjectIn.class).build();
	}

	@Bean
	public JmsItemWriter<DomObjectOut> queueItemWriter(JmsTemplate jmsTemplate) {

		return new JmsItemWriterBuilder<DomObjectOut>().jmsTemplate(jmsTemplate).build();
	}

}
