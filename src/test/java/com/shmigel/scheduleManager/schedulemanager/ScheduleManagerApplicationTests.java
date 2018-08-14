package com.shmigel.scheduleManager.schedulemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.controller.BaseController;
import com.shmigel.scheduleManager.model.dialogflow.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleManagerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(ScheduleManagerApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	private static String baseRequest = "{\n" +
			"   \"responseId\": \"fc14bdd4-6468-474c-a4e4-4620ca8f09f6\",\n" +
			"   \"queryResult\": {\n" +
			"     \"queryText\": \"do test\",\n" +
			"     \"parameters\": {\n" +
			"     },\n" +
			"     \"allRequiredParamsPresent\": true,\n" +
			"     \"fulfillmentMessages\": [{\n" +
			"       \"text\": {\n" +
			"         \"text\": [\"\"]\n" +
			"       }\n" +
			"     }],\n" +
			"     \"outputContexts\": [{\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/actions_capability_screen_output\"\n" +
			"     }, {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/actions_capability_audio_output\"\n" +
			"     }, {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/google_assistant_input_type_keyboard\"\n" +
			"     }, {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/actions_capability_media_response_audio\"\n" +
			"     }, {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/actions_capability_web_browser\"\n" +
			"     }, {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205/contexts/defaultwelcomeintent-followup\",\n" +
			"       \"lifespanCount\": 1\n" +
			"     }],\n" +
			"     \"intent\": {\n" +
			"       \"name\": \"projects/schedulemanager-5834a/agent/intents/4627920b-94bf-4fb8-894d-60c25aafee5e\",\n" +
			"       \"displayName\": \"Do test\"\n" +
			"     },\n" +
			"     \"intentDetectionConfidence\": 1.0,\n" +
			"     \"languageCode\": \"en-us\"\n" +
			"   },\n" +
			"   \"originalDetectIntentRequest\": {\n" +
			"     \"source\": \"google\",\n" +
			"     \"version\": \"2\",\n" +
			"     \"payload\": {\n" +
			"       \"isInSandbox\": true,\n" +
			"       \"surface\": {\n" +
			"         \"capabilities\": [{\n" +
			"           \"name\": \"actions.capability.MEDIA_RESPONSE_AUDIO\"\n" +
			"         }, {\n" +
			"           \"name\": \"actions.capability.WEB_BROWSER\"\n" +
			"         }, {\n" +
			"           \"name\": \"actions.capability.AUDIO_OUTPUT\"\n" +
			"         }, {\n" +
			"           \"name\": \"actions.capability.SCREEN_OUTPUT\"\n" +
			"         }]\n" +
			"       },\n" +
			"       \"inputs\": [{\n" +
			"         \"rawInputs\": [{\n" +
			"           \"query\": \"do test\",\n" +
			"           \"inputType\": \"KEYBOARD\"\n" +
			"         }],\n" +
			"         \"arguments\": [{\n" +
			"           \"rawText\": \"do test\",\n" +
			"           \"textValue\": \"do test\",\n" +
			"           \"name\": \"text\"\n" +
			"         }],\n" +
			"         \"intent\": \"actions.intent.TEXT\"\n" +
			"       }],\n" +
			"       \"user\": {\n" +
			"         \"lastSeen\": \"2018-08-13T14:28:37Z\",\n" +
			"         \"accessToken\": \"ya29.Glz4BZv4L-WWwOhk9xNSZibSHIUG-ek7G_CBXJXt_NL0z2cQrbJWrMw687iNmENL3m5rpwbq_A6UMrNy3JR0ybVG2iXolcD9Vg7tE1Mjo-KP8LcIvMF3-8OJtVKdDQ\",\n" +
			"         \"locale\": \"en-US\",\n" +
			"         \"userId\": \"ABwppHHw-SpqN9lX64G3nAigdoqZ3zlrQc4N7wUeDbtb7dMGfHg3EhIL96kHQUuRjabpsJYlt3FI3rAPukHtX7St1oqs\"\n" +
			"       },\n" +
			"       \"conversation\": {\n" +
			"         \"conversationId\": \"1534170630205\",\n" +
			"         \"type\": \"ACTIVE\",\n" +
			"         \"conversationToken\": \"[\\\"defaultwelcomeintent-followup\\\"]\"\n" +
			"       },\n" +
			"       \"availableSurfaces\": [{\n" +
			"         \"capabilities\": [{\n" +
			"           \"name\": \"actions.capability.WEB_BROWSER\"\n" +
			"         }, {\n" +
			"           \"name\": \"actions.capability.AUDIO_OUTPUT\"\n" +
			"         }, {\n" +
			"           \"name\": \"actions.capability.SCREEN_OUTPUT\"\n" +
			"         }]\n" +
			"       }]\n" +
			"     }\n" +
			"   },\n" +
			"   \"session\": \"projects/schedulemanager-5834a/agent/sessions/1534170630205\"\n" +
			" }";

	@Test
	public void baseController() throws Exception {
		mockMvc.perform(post("/").content(baseRequest)).andExpect(status().isOk());
//		Request request1 = new Request(
//				new OriginalDetectIntentRequest(
//						new Payload(
//								new User("123", "123", "123")
//						)
//					)
//				);
//		String s = new ObjectMapper().writeValueAsString(request1);
//		logger.info("Make json: {}", s);
//		Request request = new ObjectMapper().readValue(baseRequest, Request.class);
//		logger.info("Get object: {}", request.toString());
//		Request
//		new ObjectMapper().writeValueAsString();
	}

}
