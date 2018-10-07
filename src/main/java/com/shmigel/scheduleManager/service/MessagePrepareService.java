package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

@Service
public class MessagePrepareService {

    public Response liveEventMessage(Option<Event> event) {
        return new Response("");
    }

    public Response upcomingEventMessage(Event event) {
        return new Response("");
    }

    private String prettyEvent(Event event) {
        return event.getSummary();
    }

}
