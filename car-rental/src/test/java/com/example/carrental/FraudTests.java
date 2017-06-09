package com.example.carrental;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarRentalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
// 2 stub runner for messaging
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:fraud-detection")
public class FraudTests {

	@Autowired FraudListener fraudListener;
	@Autowired Sink sink;

	@Before
	public void setup() {
		fraudListener.name = "";
	}

	// 1 - the same pojo for serialization and deserialization
	// Run the app with real rabbit against the producer
	@Test
	public void should_store_info_about_fraud() {
		Fraud fraud = new Fraud("marcin");

		this.sink.input().send(MessageBuilder.withPayload(fraud).build());

		BDDAssertions.then(this.fraudListener.name).isEqualTo("marcin");
	}

	// 2 - stub runner for messaging
	@Autowired StubTrigger stubTrigger;
	@Autowired GoodFraudListener goodFraudListener;

	@Test
	public void should_store_info_about_fraud_via_stub_runner() {
		stubTrigger.trigger("trigger_a_fraud");

		BDDAssertions.then(this.goodFraudListener.name).isEqualTo("Long");
	}

}
