package com.example.carrental;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest(classes = CarRentalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
// 2 stub runner for messaging
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:fraud-detection")
public class FraudTests {

	@Autowired FraudListener fraudListener;
	@Autowired InputDestination sink;

	@BeforeEach
	public void setup() {
		fraudListener.name = "";
	}

	// 1 - the same pojo for serialization and deserialization
	// Run the app with real rabbit against the producer
	@Test
	public void should_store_info_about_fraud() {
		Fraud fraud = new Fraud("marcin");

		this.sink.send(MessageBuilder.withPayload(fraud).build());

		BDDAssertions.then(this.fraudListener.name).isEqualTo("marcin");
	}

	// 2 - stub runner for messaging
	@Autowired StubTrigger stubTrigger;
	@Autowired RestTemplate restTemplate;

	// (4) - stub runner for discovery client
	@Test
	@Disabled
	public void should_retrieve_list_of_frauds_from_stub_via_discovery() {
		ResponseEntity<String> entity = this.restTemplate.exchange(RequestEntity
				.get(URI.create("http://fraud-detection/frauds")).build(), String.class);

		BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
		BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
	}
}
