package com.shmigel.scheduleManager.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.shmigel.scheduleManager.GoogleCalendar;
import com.shmigel.scheduleManager.model.EventController;
import com.shmigel.scheduleManager.model.EventMapping;
import com.shmigel.scheduleManager.model.dialogflow.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.Map;

@EventController
public class DialogflowEventController {

    @Lazy
    @Autowired
    private GoogleCalendar c;

    @EventMapping("TEST_EVENT")
    public Response testEvent() {

        return new Response("Test is successful");
    }

    @EventMapping("ADD_EVENT")
    public Response addEvent(Map<String, String> parameters) {
        return new Response("Event's scheduled");
    }

    @EventMapping("CREATE_SCHEDULE")
    public Response createSchedule() throws IOException {
        System.out.println(c.getCalendars());
        return new Response("Schedule's created");
    }

    @EventMapping("ADD_USER")
    public Response addUser(Map<String, String> parameters) {
        return new Response("User's added");
    }
}
