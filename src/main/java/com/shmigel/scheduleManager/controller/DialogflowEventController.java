package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.Map;

@EventController
public class DialogflowEventController {

    @Lazy
    @Autowired
    private CalendarService calendar;

    @EventMapping("TEST_EVENT")
    public Response testEvent() {

        return new Response("OnResponse is successful");
    }

    @EventMapping("ADD_EVENT")
    public Response addEvent(Map<String, String> parameters) {
        return new Response("Event's scheduled");
    }

    @EventMapping("CREATE_SCHEDULE")
    public Response createSchedule() throws IOException {
        calendar.getCalendar();
        return new Response("Schedule's created");
    }

    @EventMapping("ADD_USER")
    public Response addUser(Map<String, String> parameters) {
        return new Response("User's added");
    }
}
