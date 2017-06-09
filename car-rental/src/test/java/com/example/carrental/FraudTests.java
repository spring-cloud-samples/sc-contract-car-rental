package com.example.carrental;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarRentalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class FraudTests {

	@Autowired FraudListener fraudListener;

	@Test
	public void should_store_info_about_fraud() {
		Fraud fraud = new Fraud("marcin");

		this.fraudListener.fraud(fraud);

		BDDAssertions.then(this.fraudListener.name).isEqualTo("marcin");
	}

}
