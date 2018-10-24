package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.Tuple;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.dialogflow.model.TextResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponseBuilder;
import com.shmigel.scheduleManager.dialogflow.model.response.SimpleResponse;
import com.shmigel.scheduleManager.dialogflow.model.GoogleResponse;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventController;
import com.shmigel.scheduleManager.dialogflow.model.annotation.EventMapping;
import com.shmigel.scheduleManager.service.ResponsePrepareService;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.control.Either;
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
    public TextResponse testEvent() {
        calendar.allFuncTest();
        return new TextResponse("Test is successful");
    }

    @EventMapping("LIVE_EVENT")
    public TextResponse liveEvent() {
        return messagePrepare.liveEventMessage(calendar.liveEvent());
    }

    @EventMapping("UPCOMING_EVENT")
    public TextResponse upcomingEvent() {
        return messagePrepare.upcomingEventMessage(calendar.nextEvent());
    }

    @EventMapping("ADD_EVENT")
    public TextResponse addEvent(Map<String, String> parameters) {
        return new TextResponse("Event's scheduled. Currently not supported");
    }

    @EventMapping("DAY_EVENTS")
    public Response dayEvents(Map<String, String> parameters) {
        DateTime time = new DateTime(parameters.getOrDefault("date", dateTimeUtil.now().toString()));
        Either<TextResponse, TextResponse> response
                = messagePrepare.dayEvents(calendar.dayEvents(time.getDayOfMonth()));
        if (response.isLeft()) {
            return response.getLeft();
        } else {
            return response.get();
        }
    }

    @EventMapping("EVENT")
    public TextResponse event(Map<String, String> parameters) {
        DateTime date = new DateTime(parameters.get("date"));
        int position = Double.valueOf(parameters.get("position")).intValue();
        return messagePrepare.event(calendar.event(date.getDayOfMonth(), position));
    }

    @EventMapping("ADD_USER")
    public TextResponse addUser() {
        return new TextResponse("Currently not supported");
    }

    private GoogleResponse simpleResponse(Tuple<String, String> simpleResponses) {
        return new GoogleResponse(new RichResponseBuilder(
                new RichResponse().addElement(new SimpleResponse(simpleResponses.getFirst(), simpleResponses.getSecond()))));
    }
}
