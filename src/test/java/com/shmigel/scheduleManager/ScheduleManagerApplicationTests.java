package com.shmigel.scheduleManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.dialogflow.model.*;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleManagerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ScheduleManagerApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Auth0TokenService tokenManager;

	private static String prepareJson = "{\n" +
			"  \"responseId\": \"b0e90e8a-344e-43bc-a5b7-e81e47731d68\",\n" +
			"  \"queryResult\": {\n" +
			"    \"queryText\": \"do test\",\n" +
			"    \"action\": \"TEST_EVENT\",\n" +
			"    \"parameters\": {},\n" +
			"    \"allRequiredParamsPresent\": true,\n" +
			"    \"fulfillmentMessages\": [\n" +
			"      {\n" +
			"        \"text\": {\n" +
			"          \"text\": [\n" +
			"            \"\"\n" +
			"          ]\n" +
			"        }\n" +
			"      }\n" +
			"    ],\n" +
			"    \"intent\": {\n" +
			"      \"name\": \"projects/schedule-manager-a6180/agent/intents/02c8eae7-46f7-45f9-ab4d-d8c9e5204243\",\n" +
			"      \"displayName\": \"OnResponse\"\n" +
			"    },\n" +
			"    \"intentDetectionConfidence\": 1,\n" +
			"    \"languageCode\": \"en\"\n" +
			"  },\n" +
			"  \"originalDetectIntentRequest\": {\n" +
			"    \"payload\": {}\n" +
			"  },\n" +
			"  \"session\": \"projects/schedule-manager-a6180/agent/sessions/8db5ea0d-f5c9-acf2-ee17-f6067f29dbb6\"\n" +
			"}";

	private String jsonOf(Object o) {
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	private String token = "ygGF7_9APnQUnDa5TAaqlP2EqB4xBSZb";

	private Request request(String action) {
		return new Request(
				new QueryResult(action, action, null),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK")))
		);
	}

	private Request requestWithParameters(String action, Map<String, String> parameters) {
		return new Request(
				new QueryResult(action, action , parameters),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK")))
		);
	}

	@Test
	public void getController() throws Exception {
		MvcResult test = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();
		logger.info("Return form get request: {}", test.getResponse().getContentAsString());
	}

	@Test
	public void jsonMapping() throws IOException {
		Request request = new ObjectMapper().readValue(prepareJson, Request.class);
		logger.info("Get object: {}", request.toString());
	}

	@Test
	public void baseController() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(request("CREATE_SCHEDULE"))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		assertNotNull(contentAsString);
		logger.info("Return from post request {}", contentAsString);
	}

	@Test
	public void control() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(request("TEST_EVENT"))))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		assertNotNull(content);
		logger.debug("Returned from request:"+content);
	}

	@Test
	public void testTokenManager() {
		String load = tokenManager.load(token);
		logger.debug("Receive accessToken:"+ load);
	}

	@Test
	public void rawJsonTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(requestWithParameters("ADD_USER", Collections.singletonMap("EMAIL", "sh@mail.com")))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from post request {}", contentAsString);
	}

}
