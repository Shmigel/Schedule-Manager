package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.Suggestion;
import com.shmigel.scheduleManager.dialogflow.model.response.message.SimpleResponseBuilder;
import com.shmigel.scheduleManager.dialogflow.model.response.message.SimpleResponse;
import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import com.shmigel.scheduleManager.util.DateTimeFormatters;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.control.Option;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResponsePrepareService {

    private final DateTimeUtil dateTimeUtil;

    private final EventDescriptionParser descriptionParser;

    @Autowired
    public ResponsePrepareService(DateTimeUtil dateTimeUtil, EventDescriptionParser descriptionParser) {
        this.dateTimeUtil = dateTimeUtil;
        this.descriptionParser = descriptionParser;
    }

    public DialogflowResponse liveEventMessage(Option<Event> event) {
        if (!event.isEmpty()) {
            return eventResponse("Right now you have lecture of ", event.get());
        } else
            return new RichResponse().of(
                    new SimpleResponseBuilder().say("It seams like you're free now.")
                            .pause("400ms").say("Take a breath")
                            .build());
    }

    public DialogflowResponse upcomingEventMessage(Event event) {
         return eventResponse("Your next event is ", event);
    }

    public DialogflowResponse eventByPosition(Option<Event> optionEvent, String position) {
        if (optionEvent.isEmpty())
            return new RichResponse().of(
                    new SimpleResponseBuilder().say("Couldn't find this one"));

        return eventResponse("Here is quick overview of your event of ", optionEvent.get());
    }

    public DialogflowResponse dayEvents(List<Event> dayEvents) {
        if (dayEvents.isEmpty())
            return new RichResponse().of(new SimpleResponseBuilder()
                    .say("It looks like you're free all day long. Just take a rest").build());

        DateTime start = dateTimeUtil.toJDateTime(dayEvents.get(0).getStart().getDateTime());
        SimpleResponse simpleResponse = new SimpleResponseBuilder()
                .say("Below is your brief plan for " + start.toString(DateTimeFormatters.monthDay.formatter())).pause("300ms")
                .say("You have " + dayEvents.size() + " events,")
                .newLine()
                .say(prettyNames(dayEvents))
                .newLine()
                .say("which will start at " + dateTimeUtil.startTime(dayEvents.get(0)))
                .say("and be over by " + dateTimeUtil.endTime(dayEvents.get(dayEvents.size() - 1)))
                .say("Good luck")
                .build();
        List<Suggestion> suggestions = suggestions(dayEvents.size());
        return new RichResponse().of(simpleResponse, suggestions);
    }

    /**
     * Since all solo event response has the same body of eventByPosition but with different header
     * (ex. "Right now you have lecture of ...", "Your next event is ...")
     * @param header response starting
     * @return
     */
    private DialogflowResponse eventResponse(String header, Event event) {
        DateTime dateTime = new DateTime(event.getStart().getDateTime().getValue());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        SimpleResponse simpleResponse = new SimpleResponseBuilder().say(header + event.getSummary())
                .say("and  starts at")
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter()))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter()))
                .sayIf(", its teacher is _", () -> parameters.get("teacher"))
                .sayIf(", in _", () -> parameters.get("classroom"))
                .build();

        return new RichResponse().of(simpleResponse);
    }

    private String prettyNames(List<Event> events) {
        return events.stream().map(Event::getSummary).collect(Collectors.joining("\n"));
    }

    private List<Suggestion> suggestions(int max) {
       return Stream.of(new Suggestion("First"),
                new Suggestion("Second"),
                new Suggestion("Third")).limit(max).collect(Collectors.toList());
    }

}
