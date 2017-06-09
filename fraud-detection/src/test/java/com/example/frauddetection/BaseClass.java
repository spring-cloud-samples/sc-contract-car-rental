package com.example.frauddetection;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mgrzejszczak.
 */
// Messaging base class
@RunWith(SpringRunner.class)
@AutoConfigureMessageVerifier
@SpringBootTest(classes = FraudDetectionApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BaseClass {

	// For messaging
	@Autowired FraudController controller;

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.controller);
	}

	public void triggerMethod() {
		controller.message();
	}
}
