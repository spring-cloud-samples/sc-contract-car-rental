package com.example.carrental;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by mgrzejszczak.
 */
@Disabled
@SpringBootTest(classes = CarRentalApplication.class)
public class CallingStubInEurekaTests {

	@Autowired RestTemplate restTemplate;

	// (5) - calling a stub from Eureka
	@Test
	public void should_retrieve_list_of_frauds_from_stub_via_discovery() {
		ResponseEntity<String> entity = this.restTemplate.exchange(RequestEntity
				.get(URI.create("http://fraud-detection/frauds")).build(), String.class);

		BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
		BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
	}
}
