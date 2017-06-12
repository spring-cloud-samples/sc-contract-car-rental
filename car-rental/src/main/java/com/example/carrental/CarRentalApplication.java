package com.example.carrental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
@EnableBinding({Sink.class, GoodSink.class})
@EnableDiscoveryClient
public class CarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}

	@Bean
	@LoadBalanced RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

// FOR MANUAL TESTS

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


// FOR STUB RUNNER

@Component
class GoodFraudListener {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	String name;

	@StreamListener(GoodSink.INPUT)
	public void fraud(GoodFraud fraud) {
		log.info("Got the message [{}]", fraud);
		name = fraud.surname;
	}
}

/**
 * We separate the inputs from stub runner and manual one
 */
interface GoodSink {

	String INPUT = "good_input";

	@Input(GoodSink.INPUT) SubscribableChannel input();

}

/**
 * Fraud that will fail in reality cause name should be surname
 */
class Fraud {
	public String name;

	public Fraud(String name) {
		this.name = name;
	}

	public Fraud() {
	}

	@Override public String toString() {
		return "Fraud{" + "name='" + name + '\'' + '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/**
 * Fraud that will pass in reality cause it contains a surname
 */
class GoodFraud {
	public String surname;

	public GoodFraud(String surname) {
		this.surname = surname;
	}

	public GoodFraud() {
	}

	@Override public String toString() {
		return "GoodFraud{" + "surname='" + surname + '\'' + '}';
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}

@RestController
class CarRentalController {
	private final RestTemplate restTemplate;

	CarRentalController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostMapping("/rent")
	@SuppressWarnings("unchecked")
	ResponseEntity<String> rent(@RequestBody ClientRequest request) {
		String frauds = restTemplate.getForObject("http://fraud-detection/frauds", String.class);
		if (frauds.contains(request.getName())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NO");
		}
		return ResponseEntity.ok("YES");
	}
}


class ClientRequest {
	public String name;

	public ClientRequest(String name) {
		this.name = name;
	}

	public ClientRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}