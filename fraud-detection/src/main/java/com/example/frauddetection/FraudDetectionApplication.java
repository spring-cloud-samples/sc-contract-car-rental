package com.example.frauddetection;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class FraudDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionApplication.class, args);
	}

	@Bean
	EmitterProcessor<Fraud> emitterProcessor() {
		return EmitterProcessor.create();
	}

	@Bean
	Supplier<Flux<Fraud>> output(EmitterProcessor<Fraud> emitter) {
		return () -> emitter;
	}
}
