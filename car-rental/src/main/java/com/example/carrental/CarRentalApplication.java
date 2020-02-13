package com.example.carrental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

@SpringBootApplication
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

@Component("input")
class FraudListener implements Consumer<Fraud> {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	String name;

	private void fraud(Fraud fraud) {
		log.info("Got the message [{}]", fraud);
		name = fraud.name;
	}

	@Override
	public void accept(Fraud t) {
		fraud(t);
	}
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