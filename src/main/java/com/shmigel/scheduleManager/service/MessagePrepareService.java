package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.Tuple3;
import io.vavr.control.Option;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagePrepareService {

    @Autowired
    private DateTimeUtil dateTimeUtil;

    public Response liveEventMessage(Option<Event> event) {
        if (!event.isEmpty()) {
            return new Response(new Speech()
                    .say("Right now you have lecture of "+ event.get().getSummary())
                    .say("in "+event.get().getDescription())
                    .build());
        } else {
            return new Response(
                    new Speech().say("It seams like you're free now.")
                            .pause("400ms").say("Take a breath")
                            .build());
        }
    }

    /**
     * Get response for upcoming event call
     * Collect base time information of given event,
     * and prepare response based on this
     *
     * @param event
     * @return
     */
    public Response upcomingEventMessage(Event event) {
        DateTime dateTime = new DateTime(event.getStart().getDateTime().getValue());
        return new Response(new Speech().say("Your next event of " + event.getSummary())
                .say("starts at")
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter()))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter()))
                .build());
    }

    public Response dayEvents(List<Event> todayEvents) {
        if (!todayEvents.isEmpty()) {
            DateTime start = dateTimeUtil.toJDateTime(todayEvents.get(0).getStart().getDateTime());
            return new Response(new Speech().say("Here is your brief plan for "+ start.toString(DateTimeFormatters.monthDay.formatter())).pause("300ms")
                    .say("You have " + todayEvents.size() + " events,")
                    .say("which start at "+ dateTimeUtil.startTime(todayEvents.get(0)))
                    .say("and will end up to "+ dateTimeUtil.endTime(todayEvents.get(todayEvents.size()-1)))
                    .say("Good luck")
                    .build());
        } else
            return new Response("It looks like you're free all day. Just take a rest");
    }

    private String toPrettyString(Event event) {
        if (event == null) return "null";
        return event.getSummary() +"\n ("+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime() +") "+ event.getId();
    }

}
