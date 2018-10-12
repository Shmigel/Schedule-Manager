package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.service.MessagePrepareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.Map;

@EventController
public class DialogflowEventController {

    private final CalendarService calendar;

    private final MessagePrepareService messagePrepare;

    @Lazy
    @Autowired
    public DialogflowEventController(CalendarService calendar, MessagePrepareService messagePrepare) {
        this.calendar = calendar;
        this.messagePrepare = messagePrepare;
    }

    @EventMapping("TEST_EVENT")
    public Response testEvent() {
        calendar.allFuncTest();
        return new Response("Test is successful");
    }

    @EventMapping("LIVE_EVENT")
    public Response liveEvent() {
        return messagePrepare.liveEventMessage(calendar.liveEvent());
    }

    @EventMapping("UPCOMING_EVENT")
    public Response upcomingEvent() {
        return messagePrepare.upcomingEventMessage(calendar.nextEvent());
    }

    @EventMapping("ADD_EVENT")
    public Response addEvent(Map<String, String> parameters) {
        return new Response("Event's scheduled. Currently not supported");
    }

    @EventMapping("TODAY_EVENTS")
    public Response todayEvents() {
        return messagePrepare.todayEvents(calendar.todayEvents());
    }

    @EventMapping("TOMORROW_EVENTS")
    public Response tomorrowEvents() {
        return messagePrepare.tomorrowEvents(calendar.tomorrowEvents());
    }

    @EventMapping("ADD_USER")
    public Response addUser() {
        return new Response("User's added. Currently not supported");
    }
}
