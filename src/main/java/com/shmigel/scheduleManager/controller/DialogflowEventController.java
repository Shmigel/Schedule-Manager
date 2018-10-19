package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.service.MessagePrepareService;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@EventController
public class DialogflowEventController {

    private final CalendarService calendar;

    private final MessagePrepareService messagePrepare;

    private final  DateTimeUtil dateTimeUtil;

    @Lazy
    @Autowired
    public DialogflowEventController(CalendarService calendar,
                                     MessagePrepareService messagePrepare,
                                     DateTimeUtil dateTimeUtil) {
        this.calendar = calendar;
        this.messagePrepare = messagePrepare;
        this.dateTimeUtil = dateTimeUtil;
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

    @EventMapping("DAY_EVENTS")
    public Response dayEvents(Map<String, String> parameters) {
        DateTime time = new DateTime(parameters.getOrDefault("date", dateTimeUtil.now().toString()));
        return messagePrepare.dayEvents(calendar.dayEvents(time.getDayOfMonth()));
    }

    @EventMapping("EVENT")
    public Response event(Map<String, String> parameters) {
        DateTime date = new DateTime(parameters.get("date"));
        int position = Double.valueOf(parameters.get("position")).intValue();
        return new Response(calendar.event(date.getDayOfMonth(), position).toString());
    }

    @EventMapping("ADD_USER")
    public Response addUser() {
        return new Response("User's added. Currently not supported");
    }
}
