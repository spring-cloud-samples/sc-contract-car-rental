package com.example.carrental;

import java.net.URI;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.client.WireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// 2 - added WireMock stub
@AutoConfigureWireMock(port = 6543)
@DirtiesContext
public class RentACarTests {

	@Test
	public void should_retrieve_list_of_frauds() {
		// 2 - added WireMock stub - the test is passing
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/fraud"))
				.willReturn(WireMock.aResponse().withBody("[\"marcin\", \"josh\"]").withStatus(201)));

		// 1 - not passing test
		ResponseEntity<String> entity = new RestTemplate().exchange(RequestEntity
		.get(URI.create("http://localhost:6543/fraud")).build(), String.class);

		BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
		BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\", \"josh\"]");
	}

	// 3 - Run against running instance (port 6544)
	@Test
	public void should_fail_to_reach_fraud() {
		// This will fail
		try {
			ResponseEntity<String> entity = new TestRestTemplate().exchange(RequestEntity
					.get(URI.create("http://localhost:6544/fraud")).build(), String.class);
			BDDAssertions.fail("should fail");
		} catch (ResourceAccessException e) {

		}
		//BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
		//BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
	}

	@RegisterExtension public static StubRunnerExtension stubRunner = new StubRunnerExtension()
			.downloadStub("com.example", "fraud-detection")
			.withPort(6545)
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);

	// Show how stub runner works (port 6545)
	// Test will fail due to typo for `/fraud` -> remember to call `/frauds`
	@Test
	public void should_retrieve_list_of_frauds_from_stub() {
		// 4 - passing test
		ResponseEntity<String> entity = new RestTemplate().exchange(RequestEntity
		.get(URI.create("http://localhost:6545/frauds")).build(), String.class);

		BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(201);
		BDDAssertions.then(entity.getBody()).isEqualTo("[\"marcin\",\"josh\"]");
	}

	// (3) - REST DOCS
	@Test
	public void should_trigger_a_message() {
		// 4 - passing test
		ResponseEntity<String> entity = new RestTemplate().exchange(RequestEntity
		.post(URI.create("http://localhost:6545/message")).build(), String.class);

		BDDAssertions.then(entity.getStatusCode().value()).isEqualTo(200);
	}

}