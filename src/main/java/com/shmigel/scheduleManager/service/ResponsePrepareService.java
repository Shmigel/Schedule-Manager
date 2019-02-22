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
            Map<String, String> parameters = descriptionParser.split(event.get().getDescription());
            SimpleResponse build = new SimpleResponseBuilder()
                    .say("Right now you have lecture of " + event.get().getSummary())
                    .sayIf("in _", () -> parameters.get("place"))
                    .sayIf("its author is _", () -> parameters.get("author"))
                    .build();
            return new RichResponse().of(build);
        } else
            return new RichResponse().of(
                    new SimpleResponseBuilder().say("It seams like you're free now.")
                            .pause("400ms").say("Take a breath")
                            .build());
    }

    public DialogflowResponse upcomingEventMessage(Event event) {
        DateTime dateTime = new DateTime(event.getStart().getDateTime().getValue());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        SimpleResponse simpleResponse = new SimpleResponseBuilder().say("Your next eventByPosition is " + event.getSummary())
                .say("and  starts at")
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter()))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter()))
                .sayIf(", its author is _", () -> parameters.get("author"))
                .sayIf(", at _", () -> parameters.get("place"))
                .build();

        return new RichResponse().of(simpleResponse);
    }

    public DialogflowResponse dayEvents(List<Event> dayEvents) {
        if (dayEvents.isEmpty())
            return new RichResponse().of(new SimpleResponseBuilder()
                    .say("It looks like you're free all day long. Just take a rest").build());

        DateTime start = dateTimeUtil.toJDateTime(dayEvents.get(0).getStart().getDateTime());
        SimpleResponse simpleResponse = new SimpleResponseBuilder()
                .say("Here is your brief plan for " + start.toString(DateTimeFormatters.monthDay.formatter())).pause("300ms")
                .say("You have " + dayEvents.size() + " events,")
                .say(prettyNames(dayEvents))
                .say("which will start at " + dateTimeUtil.startTime(dayEvents.get(0)))
                .say("and be over by " + dateTimeUtil.endTime(dayEvents.get(dayEvents.size() - 1)))
                .say("Good luck")
                .build();
        List<Suggestion> suggestions = suggestions(dayEvents.size());
        return new RichResponse().of(simpleResponse, suggestions);
    }

    public DialogflowResponse event(Option<Event> optionEvent) {
        if (optionEvent.isEmpty())
            return new RichResponse().of(
                    new SimpleResponseBuilder().say("Couldn'suggestions find this one"));

        Event event = optionEvent.get();
        DateTime dateTime = dateTimeUtil.toJDateTime(event.getStart().getDateTime());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        SimpleResponse simpleResponse = new SimpleResponseBuilder()
                .say("Here is quick overview of your eventByPosition of " + event.getSummary())
                .say("on " + dateTime.toString(DateTimeFormatters.monthDay.formatter()) + ".")
                .say("Which starts at " + dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .sayIf("its author is _", () -> parameters.get("author"))
                .sayIf("in _", () -> parameters.get("place"))
                .build();

        return new RichResponse().of(simpleResponse);
    }

    private String prettyNames(List<Event> events) {
        return events.stream().limit(7).map(Event::getSummary).collect(Collectors.joining(", \n "));
    }

    private List<Suggestion> suggestions(int max) {
       return Stream.of(new Suggestion("First"),
                new Suggestion("Second"),
                new Suggestion("Third")).limit(max).collect(Collectors.toList());
    }

}
