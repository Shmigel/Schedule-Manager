package com.shmigel.scheduleManager.schedulemanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.model.dialogflow.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

	private static String prepareJson = "{\n" +
			"   \"responseId\": \"fc14bdd4-6468-474c-a4e4-4620ca8f09f6\",\n" +
			"   \"queryResult\": {\n" +
			"     \"queryText\": \"do test\",\n" +
			"     \"action\": \"TYPE_OF_ACTION\",\n" +
			"     \"parameters\": {\n" +
			"         \"PARAMETER_NAME\": \"PARAMETER_VALUE\"\n" +
			"     }\n" +
			"   },\n" +
			"   \"originalDetectIntentRequest\": {\n" +
			"     \"payload\": {\n" +
			"       \"user\": {\n" +
			"         \"lastSeen\": \"2018-08-13T14:28:37Z\",\n" +
			"         \"accessToken\": \"someToken\",\n" +
			"         \"locale\": \"en-US\",\n" +
			"         \"userId\": \"someId\"\n" +
			"       }\n" +
			"     }\n" +
			"   }\n" +
			" }";
	private String jsonOf(Object o) {
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	private String token = "KnhYhUAOxw-xh4UP694F9dlCL8Lg4C3m";

	private Request getRequest(String action) {
		return new Request(
				new QueryResult(action, action, null),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK")))
		);
	}

	private Request getRequestWithParameters(String action, Map<String, String> parameters) {
		return new Request(
				new QueryResult(action, action , parameters),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK")))
		);
	}

	@Test
	public void getController() throws Exception {
		MvcResult test = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();
		logger.info("Return form get request: {}", test.getResponse().getContentAsString());
		//		Request request1 = new Request(
//				new OriginalDetectIntentRequest(
//						new Payload(
//								new User("123", "123", "123")
//						)
//					)
//				);
//		String s = new ObjectMapper().writeValueAsString(request1);
//		logger.info("Make json: {}", s);
	}

	@Test
	public void jsonMapping() throws IOException {
		Request request = new ObjectMapper().readValue(prepareJson, Request.class);
		logger.info("Get object: {}", request.toString());
	}

	@Test
	public void baseController() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(getRequest("CREATE_SCHEDULE"))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from post request {}", contentAsString);
	}

	@Test
	public void rawJsonTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(getRequestWithParameters("ADD_USER", Collections.singletonMap("EMAIL", "sh@mail.com")))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from post request {}", contentAsString);
	}

}
