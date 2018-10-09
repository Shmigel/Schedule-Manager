package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import io.vavr.Tuple3;
import io.vavr.control.Option;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class MessagePrepareService {

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
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter))
                .build());
    }

    private String toPrettyString(Event event) {
        if (event == null) return "null";
        return event.getSummary() +"\n ("+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime() +") "+ event.getId();
    }

}
