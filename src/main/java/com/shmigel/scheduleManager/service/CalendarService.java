package com.shmigel.scheduleManager.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shmigel.scheduleManager.GoogleException;

import java.util.Date;
import java.util.List;

public class CalendarService {

    private Calendar calendar;

    private Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final static String defaultCalendarName = "ScheduleManagers' Calendar";

    public CalendarService(JacksonFactory jacksonFactory,
                           NetHttpTransport httpTransport, GoogleCredential googleCredential) {
        calendar = new Calendar(httpTransport, jacksonFactory, googleCredential);
    }

    /**
     * Return object of managed calendar
     *
     * @throws GoogleException if couldn't either load calendars or find managed calendar
     * @return object of managed calendar
     */
    private CalendarListEntry managedCalendar() {
        List<CalendarListEntry> list = Try.of(() -> calendar.calendarList().list().execute())
                .getOrElseThrow(GoogleException::new).getItems();

        return list.stream().filter(i -> i.getSummary().equals(defaultCalendarName))
                .findFirst().orElseThrow(GoogleException::new);
    }

    /**
     * Wrapper for {@link CalendarService#managedCalendar()}
     * @return id of base methods' return
     */
    private String calendarId() {
        return managedCalendar().getId();
    }

    /**
     * Return 4 next event from now, include ongoing event, order by startTime
     * @return list of 4 events
     */
    private List<Event> upcomingEvents() {
        String id = calendarId();
        DateTime now = new DateTime(new Date().getTime());

        return Try.of(() -> calendar.events().list(id)
                .setMaxResults(4)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute())
                .getOrElseThrow(GoogleException::new).getItems();
    }

    /**
     * Return live event if present.
     * Since return from {@link CalendarService#upcomingEvents()} is already ordered by startTime
     * we get first and check if it's ongoing
     *
     * @return live event if present
     */
    public Option<Event> liveEvent() {
        Event event = upcomingEvents().get(0);
        if (isOngoing(event)) {
            return Option.of(event);
        }
        return Option.none();
    }


    public Event nextEvent() {
        return upcomingEvents().stream()
                .filter(this::notOngoing).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isOngoing(Event event) {
        return event.getStart().getDateTime().getValue() <= new DateTime(System.currentTimeMillis()).getValue();
    }

    private boolean notOngoing(Event event) {
        return !isOngoing(event);
    }

    private String toPrettyString(CalendarListEntry entry) {
        if (entry == null) return "null";
        return entry.getId() +" "+ entry.getSummary();
    }

    private String toPrettyString(Event event) {
        if (event == null) return "null";
        return event.getSummary() +"\n ("+ event.getStart().getDateTime() +" - "+ event.getEnd().getDateTime() +") "+ event.getId();
    }


    public void allFuncTest() {
        logger.debug("Load {} calendars", upcomingEvents().size());
        upcomingEvents().forEach((i) -> System.out.println(toPrettyString(i)));
        logger.debug("Next event {}", nextEvent().getSummary());
        logger.debug("Live event: {}", !liveEvent().isEmpty()? liveEvent().get().getSummary() : "false");
    }
}
