package com.shmigel.scheduleManager.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import fj.data.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

//en.ukrainian#holiday@group.v.calendar.google.com

public class CalendarService {

    private Calendar calendar;

    private Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private static String defaultCalendarName = "ScheduleManagers' Calendar";

    public CalendarService(JacksonFactory jacksonFactory,
                           NetHttpTransport httpTransport, GoogleCredential googleCredential) {
        calendar = new Calendar(httpTransport, jacksonFactory, googleCredential);
    }

    /**
     * Return id of managed defaultCalendar
     *
     * @return
     */
    private CalendarListEntry defaultCalendar() {
        CalendarListEntry entry = null;
        try {
            entry = calendar.calendarList().list().execute().getItems().stream()
                    .filter((i) -> i.getSummary().equals(defaultCalendarName))
                    .findFirst().get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entry;
    }

    private String calendarId() {
        return defaultCalendar().getId();
    }

    public List<Event> upcomingEvents() {
        String id = calendarId();
        DateTime now = new DateTime(System.currentTimeMillis());
        List<Event> events = null;
        try {
             events = calendar.events().list(id)
                     .setTimeMin(now)
                     .execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void allFuncTest() {
        upcomingEvents().forEach(i -> System.out.println(toPrettyString(i)));
        System.out.println();
        System.out.println(toPrettyString(nextEvent()));
        System.out.println();
        System.out.println(toPrettyString(liveEvent().right().value()));
    }

    public Either<Object, Event> liveEvent() {
        Event event = nextEvent();

        if (event.getStart().getDateTime().getValue() <= new DateTime(System.currentTimeMillis()).getValue()) {
            return Either.right(event);
        }else
            return Either.left(null);
    }

    public Event nextEvent() {
        return upcomingEvents().get(upcomingEvents().size()-1);
    }

    private String toPrettyString(CalendarListEntry entry) {
        return entry.getId() +" "+ entry.getSummary();
    }

    private String toPrettyString(Event event) {
        return event.getId() +" "+ event.getSummary() +" "+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime();
    }

}
