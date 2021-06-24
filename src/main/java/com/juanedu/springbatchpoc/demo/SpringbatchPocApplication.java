package com.juanedu.springbatchpoc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = "com.juanedu.springbatchpoc.demo")
public class SpringbatchPocApplication {

	private static final Logger log = LoggerFactory.getLogger(SpringbatchPocApplication.class);
	
	public static void main(String[] args) {
		log.info("Starting SpringbatchPocApplication...");
		SpringApplication.run(SpringbatchPocApplication.class, args);
	}

}
