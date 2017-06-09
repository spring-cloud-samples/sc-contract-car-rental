package com.example.frauddetection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mgrzejszczak.
 */
// Write the controller
@RestController
public class FraudController {

	// Make a typo (on the consumer side it will be `/fraud`
	@GetMapping("/frauds") ResponseEntity<List<String>> frauds() {
		return ResponseEntity.status(201).body(Arrays.asList("marcin", "josh"));
	}
}
