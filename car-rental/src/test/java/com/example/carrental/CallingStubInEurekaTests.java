package com.example.carrental;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mgrzejszczak.
 */
@SpringBootTest(classes = CarRentalApplication.class)
@Testcontainers
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:fraud-detection")
class CallingStubInEurekaTests {

	@Container
	static GenericContainer container = new GenericContainer("eureka:0.0.1-SNAPSHOT").withExposedPorts(8761);

	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry) {
		registry.add("eureka.client.eureka-server-port", () -> container.getFirstMappedPort());
	}

	@Autowired
	RestTemplate restTemplate;

	// (5) - calling a stub from Eureka
	@Test
	void should_retrieve_list_of_frauds_from_stub_via_discovery() {
		System.out.println("Waiting for eureka to properly register stubs...");
		Awaitility.await()
				.timeout(140, TimeUnit.SECONDS)
				.pollDelay(20, TimeUnit.SECONDS)
				.pollInterval(1, TimeUnit.SECONDS).untilAsserted(() -> {
					try {
						ResponseEntity<String> entity = this.restTemplate.exchange(RequestEntity
								.get(URI.create("http://fraud-detection/frauds")).build(), String.class);

						BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
						BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
					} catch (Exception ex) {
						throw new AssertionError(ex);
					}
		});
	}
}
