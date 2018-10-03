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
        calendar.allFuncTest();
        return new Response("Test is successful");
    }

    @EventMapping("LIVE_EVENT")
    public Response liveEvent() {
        return new Response(calendar.liveEvent().toString());
    }

    @EventMapping("UPCOMING_EVENT")
    public Response upcomingEvent() {
        return new Response(calendar.nextEvent().toString());
    }

    @EventMapping("ADD_EVENT")
    public Response addEvent(Map<String, String> parameters) {
        return new Response("Event's scheduled. Currently not supported");
    }

    @EventMapping("CREATE_SCHEDULE")
    public Response createSchedule() throws IOException {
        return new Response("Schedule's created. Currently not supported");
    }

    @EventMapping("ADD_USER")
    public Response addUser(Map<String, String> parameters) {
        return new Response("User's added. Currently not supported");
    }
}
