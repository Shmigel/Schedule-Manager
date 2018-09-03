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
			"      \"displayName\": \"Test\"\n" +
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

	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik1ERTVOa0pDUlRJMU1qQkJSak5FTUVGR1JqQTRORU16TnpWR016a3hNemcxTVRFM01UWTJNQSJ9.eyJpc3MiOiJodHRwczovL3NjaGVkdWxlLW1hbmFnZXIuZXUuYXV0aDAuY29tLyIsInN1YiI6IkhaNWV2ams2Q1UzUm9OYmxXN0Z1WnVHdXlOeE1wbjdrQGNsaWVudHMiLCJhdWQiOiJodHRwczovL3NjaGVkdWxlLW1hbmFnZXIuZXUuYXV0aDAuY29tL2FwaS92Mi8iLCJpYXQiOjE1MzU4ODc0OTQsImV4cCI6MTUzNTk3Mzg5NCwiYXpwIjoiSFo1ZXZqazZDVTNSb05ibFc3RnVadUd1eU54TXBuN2siLCJzY29wZSI6InJlYWQ6Y2xpZW50X2dyYW50cyBjcmVhdGU6Y2xpZW50X2dyYW50cyBkZWxldGU6Y2xpZW50X2dyYW50cyB1cGRhdGU6Y2xpZW50X2dyYW50cyByZWFkOnVzZXJzIHVwZGF0ZTp1c2VycyBkZWxldGU6dXNlcnMgY3JlYXRlOnVzZXJzIHJlYWQ6dXNlcnNfYXBwX21ldGFkYXRhIHVwZGF0ZTp1c2Vyc19hcHBfbWV0YWRhdGEgZGVsZXRlOnVzZXJzX2FwcF9tZXRhZGF0YSBjcmVhdGU6dXNlcnNfYXBwX21ldGFkYXRhIGNyZWF0ZTp1c2VyX3RpY2tldHMgcmVhZDpjbGllbnRzIHVwZGF0ZTpjbGllbnRzIGRlbGV0ZTpjbGllbnRzIGNyZWF0ZTpjbGllbnRzIHJlYWQ6Y2xpZW50X2tleXMgdXBkYXRlOmNsaWVudF9rZXlzIGRlbGV0ZTpjbGllbnRfa2V5cyBjcmVhdGU6Y2xpZW50X2tleXMgcmVhZDpjb25uZWN0aW9ucyB1cGRhdGU6Y29ubmVjdGlvbnMgZGVsZXRlOmNvbm5lY3Rpb25zIGNyZWF0ZTpjb25uZWN0aW9ucyByZWFkOnJlc291cmNlX3NlcnZlcnMgdXBkYXRlOnJlc291cmNlX3NlcnZlcnMgZGVsZXRlOnJlc291cmNlX3NlcnZlcnMgY3JlYXRlOnJlc291cmNlX3NlcnZlcnMgcmVhZDpkZXZpY2VfY3JlZGVudGlhbHMgdXBkYXRlOmRldmljZV9jcmVkZW50aWFscyBkZWxldGU6ZGV2aWNlX2NyZWRlbnRpYWxzIGNyZWF0ZTpkZXZpY2VfY3JlZGVudGlhbHMgcmVhZDpydWxlcyB1cGRhdGU6cnVsZXMgZGVsZXRlOnJ1bGVzIGNyZWF0ZTpydWxlcyByZWFkOnJ1bGVzX2NvbmZpZ3MgdXBkYXRlOnJ1bGVzX2NvbmZpZ3MgZGVsZXRlOnJ1bGVzX2NvbmZpZ3MgcmVhZDplbWFpbF9wcm92aWRlciB1cGRhdGU6ZW1haWxfcHJvdmlkZXIgZGVsZXRlOmVtYWlsX3Byb3ZpZGVyIGNyZWF0ZTplbWFpbF9wcm92aWRlciBibGFja2xpc3Q6dG9rZW5zIHJlYWQ6c3RhdHMgcmVhZDp0ZW5hbnRfc2V0dGluZ3MgdXBkYXRlOnRlbmFudF9zZXR0aW5ncyByZWFkOmxvZ3MgcmVhZDpzaGllbGRzIGNyZWF0ZTpzaGllbGRzIGRlbGV0ZTpzaGllbGRzIHVwZGF0ZTp0cmlnZ2VycyByZWFkOnRyaWdnZXJzIHJlYWQ6Z3JhbnRzIGRlbGV0ZTpncmFudHMgcmVhZDpndWFyZGlhbl9mYWN0b3JzIHVwZGF0ZTpndWFyZGlhbl9mYWN0b3JzIHJlYWQ6Z3VhcmRpYW5fZW5yb2xsbWVudHMgZGVsZXRlOmd1YXJkaWFuX2Vucm9sbG1lbnRzIGNyZWF0ZTpndWFyZGlhbl9lbnJvbGxtZW50X3RpY2tldHMgcmVhZDp1c2VyX2lkcF90b2tlbnMgY3JlYXRlOnBhc3N3b3Jkc19jaGVja2luZ19qb2IgZGVsZXRlOnBhc3N3b3Jkc19jaGVja2luZ19qb2IgcmVhZDpjdXN0b21fZG9tYWlucyBkZWxldGU6Y3VzdG9tX2RvbWFpbnMgY3JlYXRlOmN1c3RvbV9kb21haW5zIHJlYWQ6ZW1haWxfdGVtcGxhdGVzIGNyZWF0ZTplbWFpbF90ZW1wbGF0ZXMgdXBkYXRlOmVtYWlsX3RlbXBsYXRlcyIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyJ9.k4baNuIs65YfivJJ72sFtAvpfB69dkToB9WK-ivZ55IGY_oEFyuiQgGl90uFf2WamkM7y84jAKS0kKpxe4FV_tx26OFGVOGmHoFR_QZm2gJJ7QyHr99pfvqlOQ4YN9WMacdzsafPbDsgair7ucpXBYOplnkyiEvPO9FidfBMwPIpIw33HiMFPRBiidTin3o_vlOUZerNQPz9lVqwNpPxcrtxoAXWASNXNSznh_foROIL2KCPEfwSxEIYfcMWTk7ThovVrsDwOz5g-yss7a0N5gH3N9aIVwEaH4OK8W_pAyKD9UYdApylr9jt1DH4S_D3jgnTq_357gwr28NHvYqXyw";

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
				.content(jsonOf(request("CREATE_SCHEDULE"))))
				.andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Return from post request {}", contentAsString);
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
