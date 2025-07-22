package com.itt.newsAggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableMethodSecurity
public class NewsAggregationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAggregationApplication.class, args);
	}

}