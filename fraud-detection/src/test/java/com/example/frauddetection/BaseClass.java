package com.example.frauddetection;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

/**
 * Created by mgrzejszczak.
 */
// Messaging base class
@AutoConfigureMessageVerifier
@SpringBootTest(classes = FraudDetectionApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public class BaseClass {

	// For messaging
	@Autowired FraudController controller;

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.controller);
	}

	public void triggerMethod() {
		controller.message();
	}
}
