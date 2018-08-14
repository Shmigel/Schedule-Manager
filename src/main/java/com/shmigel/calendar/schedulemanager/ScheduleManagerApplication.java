package com.shmigel.calendar.schedulemanager;

import com.google.api.services.calendar.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@Controller
public class ScheduleManagerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagerApplication.class, args);
		logger.info("Application started");
//		Calendar c = new Calendar();
	}

	@RequestMapping(value = "/")
	public ResponseEntity<?> commonResponse(@RequestBody String body) {
//		System.out.println(body);
		logger.info("Receive request with body: {}", body);
		return ResponseEntity.ok().body("{\n" +
				"  \"fulfillmentText\": \"This is a text response\",\n" +
				"  \"fulfillmentMessages\": [\n" +
				"    {\n" +
				"      \"card\": {\n" +
				"        \"title\": \"card title\",\n" +
				"        \"subtitle\": \"card text\",\n" +
				"        \"imageUri\": \"https://assistant.google.com/static/images/molecule/Molecule-Formation-stop.png\",\n" +
				"        \"buttons\": [\n" +
				"          {\n" +
				"            \"text\": \"button text\",\n" +
				"            \"postback\": \"https://assistant.google.com/\"\n" +
				"          }\n" +
				"        ]\n" +
				"      }\n" +
				"    }\n" +
				"  ],\n" +
				"  \"source\": \"example.com\",\n" +
				"  \"payload\": {\n" +
				"    \"google\": {\n" +
				"      \"expectUserResponse\": true,\n" +
				"      \"richResponse\": {\n" +
				"        \"items\": [\n" +
				"          {\n" +
				"            \"simpleResponse\": {\n" +
				"              \"textToSpeech\": \"this is a simple response\"\n" +
				"            }\n" +
				"          }\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    \"facebook\": {\n" +
				"      \"text\": \"Hello, Facebook!\"\n" +
				"    },\n" +
				"    \"slack\": {\n" +
				"      \"text\": \"This is a text response for Slack.\"\n" +
				"    }\n" +
				"  },\n" +
				"  \"outputContexts\": [\n" +
				"    {\n" +
				"      \"name\": \"projects/${PROJECT_ID}/agent/sessions/${SESSION_ID}/contexts/context name\",\n" +
				"      \"lifespanCount\": 5,\n" +
				"      \"parameters\": {\n" +
				"        \"param\": \"param value\"\n" +
				"      }\n" +
				"    }\n" +
				"  ],\n" +
				"  \"followupEventInput\": {\n" +
				"    \"name\": \"event name\",\n" +
				"    \"languageCode\": \"en-US\",\n" +
				"    \"parameters\": {\n" +
				"      \"param\": \"param value\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> commonPOSTResponse(@RequestBody String body) {
//		System.out.println(body);
		logger.info("Receive request with body: {}", body);
		return ResponseEntity.ok().body("{\n" +
				"  \"fulfillmentText\": \"This is a post response\",\n" +
				"  \"fulfillmentMessages\": [\n" +
				"    {\n" +
				"      \"card\": {\n" +
				"        \"title\": \"post title\",\n" +
				"        \"subtitle\": \"post text\",\n" +
				"        \"imageUri\": \"https://assistant.google.com/static/images/molecule/Molecule-Formation-stop.png\",\n" +
				"        \"buttons\": [\n" +
				"          {\n" +
				"            \"text\": \"post text\",\n" +
				"            \"postback\": \"https://assistant.google.com/\"\n" +
				"          }\n" +
				"        ]\n" +
				"      }\n" +
				"    }\n" +
				"  ],\n" +
				"  \"source\": \"example.com\",\n" +
				"  \"payload\": {\n" +
				"    \"google\": {\n" +
				"      \"expectUserResponse\": true,\n" +
				"      \"richResponse\": {\n" +
				"        \"items\": [\n" +
				"          {\n" +
				"            \"simpleResponse\": {\n" +
				"              \"textToSpeech\": \"this is a simple post response\"\n" +
				"            }\n" +
				"          }\n" +
				"        ]\n" +
				"      }\n" +
				"    },\n" +
				"    \"facebook\": {\n" +
				"      \"text\": \"Hello, Facebook!\"\n" +
				"    },\n" +
				"    \"slack\": {\n" +
				"      \"text\": \"This is a text response for Slack.\"\n" +
				"    }\n" +
				"  },\n" +
				"  \"outputContexts\": [\n" +
				"    {\n" +
				"      \"name\": \"projects/${PROJECT_ID}/agent/sessions/${SESSION_ID}/contexts/context name\",\n" +
				"      \"lifespanCount\": 5,\n" +
				"      \"parameters\": {\n" +
				"        \"param\": \"param value\"\n" +
				"      }\n" +
				"    }\n" +
				"  ],\n" +
				"  \"followupEventInput\": {\n" +
				"    \"name\": \"event name\",\n" +
				"    \"languageCode\": \"en-US\",\n" +
				"    \"parameters\": {\n" +
				"      \"param\": \"param value\"\n" +
				"    }\n" +
				"  }\n" +
				"}");
	}

}
