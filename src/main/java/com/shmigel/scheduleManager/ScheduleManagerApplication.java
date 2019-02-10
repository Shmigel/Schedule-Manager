package com.shmigel.scheduleManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.controller.BaseController;
import com.shmigel.scheduleManager.dialogflow.model.request.*;
import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.message.SimpleResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EnableCaching
@Slf4j
public class ScheduleManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagerApplication.class, args);
		ScheduleManagerApplication scheduleManagerApplication = new ScheduleManagerApplication();
		scheduleManagerApplication.test();
	}

	private void test() {
		Request today_event = request("TODAY_EVENT");
		System.out.println(jsonOf(today_event));
	}


	private String token = "pOMOfnhxY0HQqggj-OzpQ-mCjf5xZZEn";

	private Request request(String action) {
		return new Request(
				new QueryResult(action, action, null),
				new OriginalDetectIntentRequest(new Payload(new User("TIME", token, "UK", "1")))
		);
	}

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

}
