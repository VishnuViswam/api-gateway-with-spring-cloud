package com.server.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class  GatewayService{

	private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

	{
		logger.info("Gateway Application is starting.....");
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayService.class, args);
		logger.info("Gateway Application is started");
	}
}
