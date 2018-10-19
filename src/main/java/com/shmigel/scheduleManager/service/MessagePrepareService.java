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
import java.util.Map;

@Service
public class MessagePrepareService {

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private CalendarEventDescriptionParser descriptionParser;

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
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        return new Response(new Speech().say("Your next event of " + event.getSummary())
                .say("starts at")
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter()))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter()))
                .point()
                .sayIf("you author is _,", () -> parameters.get("author"))
                .sayIf("at place _", () -> parameters.get("place"))
                .build());
    }

    public Response dayEvents(List<Event> dayEvents) {
        if (!dayEvents.isEmpty()) {
            DateTime start = dateTimeUtil.toJDateTime(dayEvents.get(0).getStart().getDateTime());
            return new Response(new Speech().say("Here is your brief plan for "+ start.toString(DateTimeFormatters.monthDay.formatter())).pause("300ms")
                    .say("You have " + dayEvents.size() + " events,")
                    .say("which start at "+ dateTimeUtil.startTime(dayEvents.get(0)))
                    .say("and will end up to "+ dateTimeUtil.endTime(dayEvents.get(dayEvents.size()-1)))
                    .say("Good luck")
                    .build());
        } else
            return new Response("It looks like you're free all day long. Just take a rest");
    }

    public Response event(Event event) {
        DateTime dateTime = dateTimeUtil.toJDateTime(event.getStart().getDateTime());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        return new Response(new Speech()
                .say("Here is quick overview of your event of "+event.getSummary())
                .say("on "+dateTime.toString(DateTimeFormatters.monthDay.formatter())+".")
                .say("Which starts at "+dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .sayIf(() -> parameters.containsKey("author"), "you author is "+parameters.get("author")+",")
                .sayIf(() -> parameters.containsKey("place"), "at "+parameters.get("place")+",")
                .build());
    }

    private String toPrettyString(Event event) {
        if (event == null) return "null";
        return event.getSummary() +"\n ("+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime() +") "+ event.getId();
    }

}
