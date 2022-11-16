package com.example.carrental;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {FraudTests.Config.class, CarRentalApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
// 2 stub runner for messaging
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:fraud-detection")
@Testcontainers
class FraudTests {

	@Container
	static GenericContainer container = new GenericContainer("eureka:0.0.1-SNAPSHOT").withExposedPorts(8761);

	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry) {
		registry.add("eureka.client.eureka-server-port", () -> container.getFirstMappedPort());
	}

	@Autowired
	FraudListener fraudListener;
	@Autowired
	InputDestination sink;

	@BeforeEach
	public void setup() {
		fraudListener.name = "";
	}

	// 1 - the same pojo for serialization and deserialization
	// Run the app with real rabbit against the producer
	@Test
	void should_store_info_about_fraud() {
		Fraud fraud = new Fraud("marcin");

		this.sink.send(MessageBuilder.withPayload(fraud).build());

		BDDAssertions.then(this.fraudListener.name).isEqualTo("marcin");
	}

	// 2 - stub runner for messaging
	@Autowired
	StubTrigger stubTrigger;
	@Autowired
	RestTemplate restTemplate;

	// (4) - stub runner for discovery client
	@Test
	void should_retrieve_list_of_frauds_from_stub_via_discovery() {
		System.out.println("Waiting for eureka to properly register stubs...");
		Awaitility.await()
				.timeout(40, TimeUnit.SECONDS)
				.pollDelay(20, TimeUnit.SECONDS)
				.pollInterval(1, TimeUnit.SECONDS).untilAsserted(() -> {
					try {
						ResponseEntity<String> entity = this.restTemplate.exchange(RequestEntity
								.get(URI.create("http://fraud-detection/frauds")).build(), String.class);

						BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
						BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
					}
					catch (Exception ex) {
						throw new AssertionError(ex);
					}
				});
	}

	@Configuration(proxyBeanMethods = false)
	@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
	static class Config {

	}
}
