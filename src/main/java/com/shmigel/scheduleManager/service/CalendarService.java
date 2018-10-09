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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;

import java.util.List;


public class CalendarService {

    private CalendarService calendarService;

    @Lazy
    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    private Calendar googleCalendar;

    private Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final static String defaultCalendarName = "ScheduleManagers' Calendar";
    public CalendarService(JacksonFactory jacksonFactory,
                           NetHttpTransport httpTransport, GoogleCredential googleCredential) {
        googleCalendar = new Calendar.Builder(httpTransport, jacksonFactory, googleCredential)
                .setApplicationName("scheduleManager").build();
    }

    /**
     * Load all and find new object of managed google calendar
     *
     * @throws GoogleException if couldn't either load calendars or find managed googleCalendar
     * @return object of managed googleCalendar
     */
    private CalendarListEntry managedCalendar() {
        List<CalendarListEntry> list = Try.of(() -> googleCalendar.calendarList().list().execute())
                .getOrElseThrow(GoogleException::new).getItems();

        return list.stream().filter(i -> i.getSummary().equals(defaultCalendarName))
                .findFirst().orElseThrow(GoogleException::new);
    }

    private ThreadLocal<String> calendarIdCache = new ThreadLocal<>();

    /**
     * Return managed calendars' id with support of {@link ThreadLocal} caching.
     */
    private String calendarId() {
        return Option.of(calendarIdCache.get())
                .getOrElse(() -> {
                    calendarIdCache.set(managedCalendar().getId());
                    return calendarIdCache.get();
                });
    }

    /**
     * Return 4 next event from now, include ongoing event, order by startTime
     * @return list of 4 events
     */
    @Cacheable(value = "upcomingEvents", key = "#calendarId")
    public List<Event> upcomingEvents(String calendarId) {
        logger.info("Load new upcoming Events");
        return Try.of(() -> googleCalendar.events().list(calendarId)
                .setMaxResults(4)
                .setTimeMin(now())
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute())
                .getOrElseThrow(GoogleException::new).getItems();
    }

    /**
     * Return live event if present.
     * Since return from {@link CalendarService#upcomingEvents(String userId)} is already ordered by startTime
     * we get first and check if it's ongoing
     *
     * @return live event if present
     */
    public Option<Event> liveEvent() {
        Event event = calendarService.upcomingEvents(calendarId()).get(0);
        if (isOngoing(event)) {
            return Option.of(event);
        }
        return Option.none();
    }


    public Event nextEvent() {
        return calendarService.upcomingEvents(calendarId()).stream()
                .filter(this::notOngoing).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isOngoing(Event event) {
        return event.getStart().getDateTime().getValue() <= now().getValue();
    }

    private boolean notOngoing(Event event) {
        return !isOngoing(event);
    }

    private DateTime now() {
        return new DateTime(System.currentTimeMillis());
    }


    public void allFuncTest() {
        logger.debug("Load {} calendars", calendarService.upcomingEvents(calendarId()).size());
        calendarService.upcomingEvents(calendarId()).forEach((i) -> System.out.println(i.getId() + i.getSummary()));
        logger.debug("Next event {}", nextEvent().getSummary());
        logger.debug("Live event: {}", !liveEvent().isEmpty()? liveEvent().get().getSummary() : "false");
    }
}
