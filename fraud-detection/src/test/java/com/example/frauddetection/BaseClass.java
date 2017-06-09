package com.example.frauddetection;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

/**
 * Created by mgrzejszczak.
 */
public class BaseClass {

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new FraudController());
	}
}
