package com.example.carrental;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = CarRentalApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:fraud-detection")
public class ManualTests {

	@Autowired MockMvc mockMvc;

	@Test
	public void should_fail_for_fraud() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/rent")
				.header("Content-Type", "application/json")
				.content("{\"name\":\"marcin\"}"))
				.andExpect(MockMvcResultMatchers.status().is(401))
				.andExpect(MockMvcResultMatchers.content().string("NO"));
	}

	@Test
	public void should_pass_for_non_fraud() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/rent")
				.header("Content-Type", "application/json")
				.content("{\"name\":\"foo\"}"))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().string("YES"));
	}

}