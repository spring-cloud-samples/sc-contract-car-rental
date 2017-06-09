package com.example.carrental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
@EnableBinding(Sink.class)
public class CarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}
}

@Component
class FraudListener {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	String name;

	@StreamListener(Sink.INPUT)
	public void fraud(Fraud fraud) {
		log.info("Got the message [{}]", fraud);
		name = fraud.name;
	}
}


class Fraud {
	public String name;

	public Fraud(String name) {
		this.name = name;
	}

	public Fraud() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}