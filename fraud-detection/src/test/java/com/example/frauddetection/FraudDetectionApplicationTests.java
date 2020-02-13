package com.example.frauddetection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

@SpringBootTest
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public class FraudDetectionApplicationTests {

	@Test
	public void contextLoads() {
	}

}
