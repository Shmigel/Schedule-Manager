package com.shmigel.scheduleManager;

import com.shmigel.scheduleManager.config.DialogflowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@Controller
public class ScheduleManagerApplication {



	private static final Logger logger = LoggerFactory.getLogger(ScheduleManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagerApplication.class, args);
	}

}
