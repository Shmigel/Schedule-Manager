package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

@Service
public class MessagePrepareService {

    public Response liveEventMessage(Option<Event> event) {
        if (!event.isEmpty()) {
            return new Response("Now you have lecture of "+ event.get().getSummary() +"in"+ event.get().getDescription());
        } else {
            return new Response("You are free now. Breathe free");
        }
    }

    public Response upcomingEventMessage(Event event) {
        return new Response("Your next event of "+ event.getSummary() +" starts at"+ event.getStart().getDateTime().getValue());
    }

}
