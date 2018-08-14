package com.shmigel.scheduleManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
@Controller
public class ScheduleManagerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagerApplication.class, args);
		logger.info("Application started");
//		Calendar c = new Calendar();
	}

//	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> commonPOSTResponse(@RequestBody String body) {
//		System.out.println(body);
		logger.info("Receive request with body: {}", body);
		return ResponseEntity.ok().body(
				"{\n" +
				"  \"fulfillmentText\": \"This is a post response\"\n" +
				"}");
	}

}
