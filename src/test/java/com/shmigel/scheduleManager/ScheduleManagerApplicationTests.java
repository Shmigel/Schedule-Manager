package com.shmigel.scheduleManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.shmigel.scheduleManager.dialogflow.model.TextResponse;
import com.shmigel.scheduleManager.dialogflow.model.request.*;
import com.shmigel.scheduleManager.dialogflow.model.response.SimpleResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.Suggestion;
import com.shmigel.scheduleManager.dialogflow.model.GoogleResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponseBuilder;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.service.ResponsePrepareService;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleManagerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ScheduleManagerApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Auth0TokenService tokenManager;

	@Autowired
	private ApplicationContext context;

	@Lazy
	@Autowired
	private CalendarService calendarService;

	private String jsonOf(Object o) {
		String json = "";
		try {
			json = new ObjectMapper()
					.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	private String token = "YhHyfW_KC4e07Lm06UVvRb-hCcL6Lr6Q";

	private Request request(String action) {
		return new Request(
				new QueryResult(action, action, null),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK", "1")))
		);
	}

	private Request requestWithParameters(String action, Map<String, String> parameters) {
		return new Request(
				new QueryResult(action, action, parameters),
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
				.content(jsonOf(requestWithParameters("DAY_EVENTS", Collections.singletonMap("date", new DateTime().plusDays(2).toString())))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from request {}", contentAsString);
	}

	@Test
	public void eventsTest() throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("date", "2018-10-22T22:08:03+03:00");
		parameters.put("position", "1.0");
		MvcResult result = mockMvc.perform(post("/").contentType("application/json")
				.content(jsonOf(requestWithParameters("EVENT", parameters))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from request {}", contentAsString);
	}

	@Test
	public void test() {
		ResponsePrepareService bean = context.getBean(ResponsePrepareService.class);
		DateTimeUtil bean1 = context.getBean(DateTimeUtil.class);

		Tuple2<com.google.api.client.util.DateTime, com.google.api.client.util.DateTime> dayPeriod
				= bean1.dayPeriod(23);

		Event test_event = new Event().setSummary("FacilityDTO event").setDescription("author: Shmigel\n place: kc-2")
				.setStart(new EventDateTime().setDateTime(dayPeriod._1))
				.setEnd(new EventDateTime().setDateTime(dayPeriod._2));

		TextResponse response = bean.upcomingEventMessage(test_event);

		System.out.println(response);

		Option<Event> event = calendarService.event(25, 1);
		System.out.println(bean.event(event));
	}

	@Test
	public void richMessageTest() throws JsonProcessingException {
		GoogleResponse builder = new GoogleResponse(new RichResponseBuilder(
				new RichResponse(Collections.singletonList(new SimpleResponse("qwe", "ewq")),
						Collections.singletonList(new Suggestion("qwe")))));
		System.out.println(jsonOf(builder));
	}
}
