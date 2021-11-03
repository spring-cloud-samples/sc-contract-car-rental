package com.example.frauddetection;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mgrzejszczak.
 */
// (1) - Write the controller
@RestController
public class FraudController {

	// (1) - Make a typo (on the consumer side it will be `/fraud`
	@GetMapping(value = "/frauds", produces = "application/json")
	ResponseEntity<List<String>> frauds() {
		return ResponseEntity.status(201).body(Arrays.asList("marcin", "josh"));
	}

	// (2) - messaging
	private final StreamBridge source;

	public FraudController(StreamBridge source) {
		this.source = source;
	}

	// (2) - we're sending a message to a destination `frauds` - consumer expects `fraud`
	@PostMapping("/message")
	void message() {
		source.send("frauds", new Fraud("Long"));
	}
}

class Fraud {
	String surname;

	public Fraud(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}