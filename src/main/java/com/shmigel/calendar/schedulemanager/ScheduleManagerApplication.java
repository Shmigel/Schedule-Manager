package com.shmigel.calendar.schedulemanager;

import com.google.api.services.calendar.Calendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class ScheduleManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagerApplication.class, args);
//		Calendar c = new Calendar();
	}

	@RequestMapping(value = "/", consumes = "text/plain")
	public ResponseEntity<?> commonResponse(@RequestBody String  payload) {
		System.out.println(payload);
		return ResponseEntity.ok().build();
	}
}
