package com.shmigel.scheduleManager.service;

import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.Tuple;
import com.shmigel.scheduleManager.dialogflow.model.GoogleResponse;
import com.shmigel.scheduleManager.dialogflow.model.TextResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponseBuilder;
import com.shmigel.scheduleManager.dialogflow.model.response.SimpleResponse;
import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponsePrepareService {

    private final DateTimeUtil dateTimeUtil;

    private final EventDescriptionParser descriptionParser;

    @Autowired
    public ResponsePrepareService(DateTimeUtil dateTimeUtil, EventDescriptionParser descriptionParser) {
        this.dateTimeUtil = dateTimeUtil;
        this.descriptionParser = descriptionParser;
    }

    public TextResponse liveEventMessage(Option<Event> event) {
        if (!event.isEmpty()) {
            return new SimpleResponseBuilder()
                    .say("Right now you have lecture of "+ event.get().getSummary())
                    .say("in "+event.get().getDescription())
                    .textResponse();
        } else {
            return new SimpleResponseBuilder().say("It seams like you're free now.")
                            .pause("400ms").say("Take a breath")
                            .textResponse();
        }
    }

    public TextResponse upcomingEventMessage(Event event) {
        DateTime dateTime = new DateTime(event.getStart().getDateTime().getValue());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        return new SimpleResponseBuilder().say("Your next event is " + event.getSummary())
                .say("and  starts at")
                .sayAsTime("hm24", dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .say("on the")
                .sayAsDate("dd", dateTime.toString(DateTimeFormatters.dayOfWeak.formatter()))
                .pause("300ms", SpeechBreakStrength.STRONG)
                .sayAsDate("mmdd", dateTime.toString(DateTimeFormatters.monthDay.formatter()))
                .sayIf(", its author is _", () -> parameters.get("author"))
                .sayIf(", at _", () -> parameters.get("place"))
                .textResponse();
    }

    public Either<TextResponse, TextResponse> dayEvents(List<Event> dayEvents) {
        Either<TextResponse, GoogleResponse> response = null;
        if (dayEvents.isEmpty())
            return Either.left(new SimpleResponseBuilder()
                    .say("It looks like you're free all day long. Just take a rest").textResponse());

        DateTime start = dateTimeUtil.toJDateTime(dayEvents.get(0).getStart().getDateTime());
        return Either.right(new SimpleResponseBuilder()
                .say("Here is your brief plan for "+ start.toString(DateTimeFormatters.monthDay.formatter())).pause("300ms")
                .say("You have " + dayEvents.size() + " events,")
                .say(prettyNames(dayEvents))
                .say("which start at "+ dateTimeUtil.startTime(dayEvents.get(0)))
                .say("and will end up to "+ dateTimeUtil.endTime(dayEvents.get(dayEvents.size()-1)))
                .say("Good luck")
                .textResponse());
    }

    public TextResponse event(Option<Event> optionEvent) {
        if (optionEvent.isEmpty())
            return new SimpleResponseBuilder().say("Couldn't find this one").textResponse();

        Event event = optionEvent.get();
        DateTime dateTime = dateTimeUtil.toJDateTime(event.getStart().getDateTime());
        Map<String, String> parameters = descriptionParser.split(event.getDescription());

        return new SimpleResponseBuilder()
                .say("Here is quick overview of your event of "+event.getSummary())
                .say("on "+dateTime.toString(DateTimeFormatters.monthDay.formatter())+".")
                .say("Which starts at "+dateTime.toString(DateTimeFormatters.hourMinute.formatter()))
                .sayIf(() -> parameters.containsKey("author"), "you author is "+parameters.get("author")+",")
                .sayIf(() -> parameters.containsKey("place"), "at "+parameters.get("place")+",")
                .textResponse();
    }

    private String toPrettyString(Event event) {
        if (event == null) return "null";
        return event.getSummary() +"\n ("+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime() +") "+ event.getId();
    }

    private String prettyNames(List<Event> events) {
        return events.stream().limit(7).map(Event::getSummary).collect(Collectors.joining(", \n"));
    }

    private GoogleResponse simpleResponse(SimpleResponse simpleResponses) {
        return new GoogleResponse(new RichResponseBuilder(
                new RichResponse().addElement(simpleResponses)));
    }

}
