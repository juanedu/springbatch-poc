package com.juanedu.springbatchpoc.demo;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@EnableJms
@Configuration
public class ActiveMQConfig {

	// Serialize message content to json using TextMessage
	@Bean 
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public JmsTemplate jmsTemplateIn(ConnectionFactory connectionFactory) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
		cachingConnectionFactory.afterPropertiesSet();
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
		jmsTemplate.setDefaultDestinationName("domObjectsIn");
		jmsTemplate.setReceiveTimeout(5000L);
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		return jmsTemplate;
	}
	
	@Bean
	public JmsTemplate jmsTemplateOut(ConnectionFactory connectionFactory) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
		cachingConnectionFactory.afterPropertiesSet();
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
		jmsTemplate.setDefaultDestinationName("domObjectsOut");
		jmsTemplate.setReceiveTimeout(5000L);
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		return jmsTemplate;
	}
}
