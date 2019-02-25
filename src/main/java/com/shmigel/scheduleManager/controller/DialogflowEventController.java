package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.service.ResponsePrepareService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

@EventController
public class DialogflowEventController {

    private CalendarService calendar;

    private ResponsePrepareService messagePrepare;

    @Autowired
    public DialogflowEventController(CalendarService calendar,
                                     ResponsePrepareService messagePrepare) {
        this.calendar = calendar;
        this.messagePrepare = messagePrepare;
    }

    @EventMapping("TEST_EVENT")
    public DialogflowResponse testEvent() {
        calendar.allFuncTest();
        return new DialogflowResponse("Test is successful", null);
    }

    @EventMapping("LIVE_EVENT")
    public DialogflowResponse liveEvent() {
        return messagePrepare.liveEventMessage(calendar.liveEvent());
    }

    @EventMapping("UPCOMING_EVENT")
    public DialogflowResponse upcomingEvent() {
        return messagePrepare.upcomingEventMessage(calendar.nextEvent());
    }

    @EventMapping("ADD_EVENT")
    public DialogflowResponse addEvent() {
        return new DialogflowResponse("Event's scheduled. Currently not supported.", null);
    }

    @EventMapping("DAY_EVENTS")
    public DialogflowResponse dayEvents(String date) {
        DateTime time = new DateTime(date);
        return messagePrepare.dayEvents(calendar.dayEvents(time.getDayOfMonth()));
    }

    @EventMapping("EVENT")
    public DialogflowResponse event(String date, String position) {
        return messagePrepare.eventByPosition(calendar.eventByPosition(new DateTime(date).getDayOfMonth(),
                Double.valueOf(position).intValue()), position);
    }

    @EventMapping("ADD_USER")
    public DialogflowResponse addUser(String email) {
        calendar.addUser(email, "reader");
        return new DialogflowResponse("We did our part of deal.", null);
    }

}
