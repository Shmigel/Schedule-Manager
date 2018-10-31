package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.service.ResponsePrepareService;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@EventController
public class DialogflowEventController {

    private CalendarService calendar;

    private ResponsePrepareService messagePrepare;

    private DateTimeUtil dateTimeUtil;

    private BaseController controller;

    @Lazy
    @Autowired
    public DialogflowEventController(CalendarService calendar,
                                     ResponsePrepareService messagePrepare,
                                     DateTimeUtil dateTimeUtil, BaseController controller) {
        this.calendar = calendar;
        this.messagePrepare = messagePrepare;
        this.dateTimeUtil = dateTimeUtil;
        this.controller = controller;
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
    public DialogflowResponse addEvent(Map<String, String> parameters) {
        return new DialogflowResponse("Event's scheduled. Currently not supported", null);
    }

    @EventMapping("DAY_EVENTS")
    public DialogflowResponse dayEvents(Map<String, String> parameters) {
        DateTime time = new DateTime(parameters.getOrDefault("date", dateTimeUtil.now().toString()));
        return messagePrepare.dayEvents(calendar.dayEvents(time.getDayOfMonth()));
    }

    @EventMapping("EVENT")
    public DialogflowResponse event(Map<String, String> parameters) {
        DateTime date = new DateTime(parameters.get("date"));
        int position = Double.valueOf(parameters.get("position")).intValue();
        return messagePrepare.event(calendar.event(date.getDayOfMonth(), position));
    }

    @EventMapping("ADD_USER")
    public DialogflowResponse addUser(Map<String, String> parameters) {
        String email = parameters.get("email");
        calendar.addUser(email, "reader");
        return new DialogflowResponse("We did our part of deal.", null);
    }

}
