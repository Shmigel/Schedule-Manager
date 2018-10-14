package com.shmigel.scheduleManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.dialogflow.model.*;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import com.shmigel.scheduleManager.service.Speech;
import org.joda.time.DateTime;
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

	private String jsonOf(Object o) {
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	private String token = "xe9gmbcsEDy95L4w5TFO1zPkp1Y2nUs1";

	private Request request(String action) {
		return new Request(
				new QueryResult(action, action, null),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK", "1")))
		);
	}

	private Request requestWithParameters(String action, Map<String, String> parameters) {
		return new Request(
				new QueryResult(action, action , parameters),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK", "1")))
		);
	}

	@Test
	public void getController() throws Exception {
		MvcResult test = mockMvc.perform(get("/")).andExpect(status().isOk()).andReturn();
		logger.info("Return form get request: {}", test.getResponse().getContentAsString());
	}

	@Test
	public void baseController() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(request("TEST_EVENT"))))
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
		Tuple<String, String> load = tokenManager.loadTokens(token);
		logger.debug("Receive accessToken:"+ load.getFirst()+", refresh:"+load.getSecond());
	}

	@Test
	public void upcomingEventTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(request("UPCOMING_EVENT"))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return post request {}", contentAsString);
	}

	@Test
	public void liveEventTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(request("LIVE_EVENT"))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from request {}", contentAsString);
	}

	@Test
	public void dayEventsTest() throws Exception {
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(requestWithParameters("DAY_EVENTS", Collections.singletonMap("date", new DateTime().minusDays(3).toString())))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from request {}", contentAsString);
	}

}
